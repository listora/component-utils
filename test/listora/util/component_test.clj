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
