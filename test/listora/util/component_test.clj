(ns listora.util.component-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [listora.util.component :refer :all]))

(defrecord TestComponent []
  component/Lifecycle
  (start [component] (assoc component :running? true))
  (stop  [component] (dissoc component :running?)))

(deftest test-component-name
  (is (= (component-name (->TestComponent)) "TestComponent")))

(deftest test-verbose-start
  (let [component (->TestComponent)]
    (testing "return value"
      (with-out-str
        (is (= (verbose-start component) (component/start component)))))
    (testing "logging"
      (let [out (with-out-str (verbose-start component))]
        (is (re-find #"Starting TestComponent…" out))))))

(deftest test-verbose-stop
  (let [component (component/start (->TestComponent))]
    (testing "return value"
      (with-out-str
        (is (= (verbose-stop component) (component/stop component)))))
    (testing "logging"
      (let [out (with-out-str (verbose-stop component))]
        (is (re-find #"Stopping TestComponent…" out))))))

(defrecord AnotherTestComponent []
  component/Lifecycle
  (start [component] (assoc component :running? true))
  (stop  [component] (dissoc component :running?)))

(def test-system
  (-> (component/system-map
       :test-component (->TestComponent)
       :another-test-component (->AnotherTestComponent))
      (component/system-using
       {:another-test-component [:test-component]})))

(deftest test-verbose-start-system
  (testing "return value"
    (with-out-str
      (is (= (verbose-start-system test-system)
             (component/start-system test-system)))))
  (testing "logging"
    (let [out (with-out-str (verbose-start-system test-system))]
      (is (re-find #"Starting TestComponent…" out))
      (is (re-find #"Starting AnotherTestComponent…" out))
      (is (re-find #"(?s)TestComponent.*AnotherTestComponent" out)))))

(deftest test-verbose-stop-system
  (let [system (component/start-system test-system)]
    (testing "return value"
      (with-out-str
        (is (= (verbose-stop-system system)
               (component/stop-system system)))))
    (testing "logging"
      (let [out (with-out-str (verbose-stop-system system))]
        (is (re-find #"Stopping AnotherTestComponent…" out))
        (is (re-find #"Stopping TestComponent…" out))
        (is (re-find #"(?s)AnotherTestComponent.*TestComponent" out))))))

(def verbose-test-system
  (-> (verbose-system-map
       :test-component (->TestComponent)
       :another-test-component (->AnotherTestComponent))
      (component/system-using
       {:another-test-component [:test-component]})))

(deftest test-verbose-system
  (testing "logging"
    (let [out (with-out-str (-> verbose-test-system
                                component/start
                                component/stop))]
      (is (re-find #"Starting TestComponent…" out))
      (is (re-find #"Starting AnotherTestComponent…" out))
      (is (re-find #"Stopping AnotherTestComponent…" out))
      (is (re-find #"Stopping TestComponent…" out)))))
