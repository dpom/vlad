(ns vlad.validations
  (:use [vlad.validation-types])
  (:require [clojure.string :as str]))

(defn present
  "Checks that the string found at `selector` is not blank.
  
  Example:
  
    (validate (present :name)
              {:name \"Vlad\"})"
  [selector]
  (predicate selector str/blank?  {:type ::present}))

(defn length-over 
  "Checks that the `count` of the value found at `selector` is over `size`."
  [size selector]
  (predicate selector #(> size (count %)) {:type ::length-over :size size}))

(defn length-under 
  "Checks that the `count` of the value found at `selector` is under `size`."
  [size selector]
  (predicate selector #(< size (count %)) {:type ::length-under :size size}))

(defn length-in 
  "Checks that the `count` of the value found at `selector` is over `lower` and
  under `upper`. No checking is done that `lower` is lower than `upper`. This
  validator may return multiple errors"
  [lower upper selector]
  (join
    (length-over  lower selector)
    (length-under upper selector)))

(defn one-of
  "Checks that the value found at `selector` is found within `set`"
  [set selector]
  (predicate selector #(not (contains? set %)) {:type ::one-of :set set}))

(defn not-of
  "Checks that the value found at `selector` is not found within `set`"
  [set selector]
  (predicate selector #(contains? set %) {:type ::not-of :set set}))

(defn equals-value
  "Checks that the value found at `selector` is equal to the `value` that you
  provide."
  [value selector]
  (predicate selector #(not (= value %)) {:type ::equals-value :value value}))

(defn equals-field
  "Checks that the values found at each of your selectors are equal to each
  other"
  [first-selector second-selector]
  (fn [data]
    (let [first-value  (get-in data first-selector)
          second-value (get-in data second-selector)]
      (if (= first-value second-value)
          []
          [{:type ::equals-field
            :first-selector first-selector
            :second-selector second-selector}]))))

(defn matches
  "Checks that the value found at `selector` is a regex match for `pattern`.
  This uses clojure's `re-matches` function which may not behave as you expect.
  Your pattern will have to match the whole string found at `selector` to count
  as a match."
  [pattern selector]
  (predicate selector #(nil? (re-matches pattern %))
             {:type ::matches :pattern pattern}))

(defn number
  "Checks that the value found at `selector` is a number."
  [selector]
  (predicate selector #(not (number? %)) {:type ::number}))

(defn nil-value
  "Checks that the value found at `selector` is nil."
  [selector]
  (predicate selector #(not (nil? %)) {:type ::nil-value}))


(defn greater-value
  "Checks that the value found at `selector` is  greater than `vmin`."
  [vmin selector]
  (predicate selector #(> vmin %) {:type ::greater-value :vmin vmin}))

(defn greater-field
  "Checks that the values found at first selector is greater than the second one."
  [first-selector second-selector]
  (fn [data]
    (let [first-value  (get-in data first-selector)
          second-value (get-in data second-selector)]
      (if (> first-value second-value)
          []
          [{:type ::greater-field
            :first-selector first-selector
            :second-selector second-selector}]))))

(defn less-value
  "Checks that the value found at `selector` is less than `vmax`."
  [vmax selector]
  (predicate selector #(< vmax %) {:type ::less-value :vmax vmax}))

(defn between-value
  "Checks that the value found at `selector` is  greater than `vmin`
   and less than `vmax`."
   [vmin vmax selector]
   (join
    (greater-value vmin selector)
    (less-value vmax selector)))

(defn equal-or-greater-value
   "Checks that the value found at `selector` is equal or  greater than `vmin`."
  [vmin selector]
  (alternate
   (equals-value vmin selector)
   (greater-value vmin selector)))

(defn num-greater
  "Checks that the value found at `selector` is a number and is greater than `vmin`."
  [vmin selector]
  (chain
   (number selector)
   (greater-value vmin selector)))

(defn num-less
  "Checks that the value found at `selector` is a number and is  less than `vmax`."
  [vmax selector]
  (chain
   (number selector)
   (less-value vmax selector)))

(defn num-between
  "Checks that the value found at `selector` is a number and is  greater than `vmin`
   and  less than `vmax`."
   [vmin vmax selector]
   (chain
    (number selector)
    (between-value vmin vmax selector)))

