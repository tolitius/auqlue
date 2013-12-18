(ns auqlue.dbnator
  (:use [datomic.api :only (db) :as d]))

;; get (re) initialized in "connect-to"
;; this id not an atom to enable threads to connect to other DBs
(def ^{:dynamic true :doc "datomic connection available to all auqlue db functions."} *conn*)

(def ref-type 20) ;; Datomic constant for a "ref type"

(defn connect-to [uri]
  (d/create-database uri)
  (def ^{:dynamic true} *conn* (d/connect uri)))

(defn db-now [] (db *conn*))

;; getting behind the query engine down to datomic indicies
(defn lookup [?a _ a v] 
  "e.g. (lookup :prezi/title :by :prezi/event \"Strange Loop\")"
  (let [d (db *conn*)] 
    (map #(map :v (d/datoms d :eavt (:e %) ?a)) 
         (d/datoms d :avet a v))))

(defn lookup-via-ref [?a _ r _ a v] 
  "e.g. (lookup-via-ref :prezi/title :via :prezi/questions :by :qa/question \"WTF is Monad?\")"
  (let [d (db *conn*)] 
    (map #(-> (d/datoms d :eavt 
                        (-> (d/datoms d :avet r (:e %)) first :e) ?a) 
              first :v) 
         (d/datoms d :avet a v))))

(defn q [query & args] 
  (apply d/q query (db-now) args))

(defn q-as-of [query date & args] 
  (apply d/q query (d/as-of (db-now) date) args))

(defn q-history [query & args] 
  (apply d/q query (d/history (db-now)) args))

(defn entity [id] 
  (d/entity (db-now) id))

(defn touch [results]
  "takes 'entity ids' results from a query 
    e.g. '#{[272678883689461] [272678883689462] [272678883689459] [272678883689457]}'"
  (map #(-> % first entity d/touch) results))

(defn touch-id [id]
  "takes in an enitity id as a number"
  (d/touch (entity id)))

(defn de-namespace
  "{:prezi/event \"Strange Loop\", :prezi/speaker \"Rich Hickey\", :prezi/title \"Simple Made Easy\", :db/id 272678883689451}
       to
   {:event \"Strange Loop\", :speaker \"Rich Hickey\", :title \"Simple Made Easy\", :id 272678883689451}"
  [m]
  (into {} (for [[k v] m] 
             [(-> k name keyword) v])))

(defn with-tx [tx-data]
  (when (seq tx-data)
    @(d/transact *conn* tx-data)))

(defn add-to-entity [id attr value]
  @(d/transact *conn*
    [[:db/add id attr value]]))

(defn retract-from-entity [id attr value]
  @(d/transact *conn*
    [[:db/retract id attr value]]))

(defn add-fact [e a v t] 
  "has a time 't' component to it, which should be no younger than the latest tx, and not in the future"
  @(d/transact *conn* [
     {:db/id (d/tempid "db.part/tx") :db/txInstant t} 
     {:db/id e a v}]))

(defn remove-fact [e a v t] 
  "has a time 't' component to it, which should be no younger than the latest tx, and not in the future"
  @(d/transact *conn* [
     {:db/id (d/tempid "db.part/tx") :db/txInstant t} 
     [:db/retract e a v]]))

(defn txmap [tx] 
  (zipmap [:timestamp :attr :value :added? :ftype] tx))

(defn timeline [id] 
  (map txmap 
       (d/q '[:find ?when ?ident ?v ?added ?type 
              :in $ ?e 
              :where [?e ?a ?v ?tx ?added]
                     [?tx :db/txInstant ?when] 
                     [?a :db/ident ?ident]
                     [?a :db/valueType ?type]] 
            (d/history (db-now)) id)))

(defn- find-refs [tl] 
  (filter #(= (:ftype %) ref-type) tl))

(defn- with-no-refs [tl] 
  (filter #(not= (:ftype %) ref-type) tl))

(defn- dref [{:keys [ftype value] :as fact}] 
  (if (= ftype ref-type) (timeline value)))

(defn ref-walk [t-line] 
  (with-no-refs (concat t-line 
    (reduce concat
            (map dref (find-refs t-line))))))
