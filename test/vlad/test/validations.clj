(ns vlad.test.validations
  (:use [midje.sweet :only [tabular fact just contains]]
        [vlad validations validation-types]))

(tabular
  (fact (validate ?validator {:name "Chris" :confirm_name "Brad"}) => ?errors)
  ?validator ?errors

  (present [:name])
  []                   

  (present [:age])
  [{:type :vlad.validations/present
    :selector [:age]}] 

  (length-over 4 [:name])
  []                   

  (length-over 9 [:name])
  [{:type :vlad.validations/length-over
    :size 9
    :selector [:name]}] 

  (length-under 9 [:name])
  []                   

  (length-under 4 [:name])
  [{:type :vlad.validations/length-under
    :size 4
    :selector [:name]}] 

  (length-in 4 9 [:name])
  []                   

  (length-in 9 4 [:name])
  [{:type :vlad.validations/length-over
    :size 9
    :selector [:name]}
   {:type :vlad.validations/length-under
    :size 4
    :selector [:name]}]

  (one-of #{"Chris" "Fred"} [:name])
  []

  (one-of #{"Thelma" "Luise"} [:name])
  [{:type :vlad.validations/one-of
    :set #{"Thelma" "Luise"}
    :selector [:name]}]

  (not-of #{"Thelma" "Luise"} [:name])
  []

  (not-of #{"Chris" "Fred"} [:name])
  [{:type :vlad.validations/not-of
    :set #{"Chris" "Fred"}
    :selector [:name]}]

  (equals-value "Chris" [:name])
  []

  (equals-value "Maddy" [:name])
  [{:type :vlad.validations/equals-value
    :value "Maddy"
    :selector [:name]}] 

  (equals-field [:name] [:name])
  []

  (equals-field [:name] [:confirm_name])
  [{:type :vlad.validations/equals-field
    :first-selector [:name]
    :second-selector [:confirm_name]}]
  
  (matches #"..ris" [:name])
  []
  
  (matches #"andy" [:name])
  (just [(contains {:type :vlad.validations/matches
                              :selector [:name]})]))

(tabular
(fact (validate ?validator {:num1 7 :num2 nil :num3 "abc" :num4 9}) => ?errors)
?validator                         ?errors

(number [:num1])                    []

(number [:num2])                    [{:selector [:num2], :type :vlad.validations/number}]

(number [:num3])                    [{:selector [:num3], :type :vlad.validations/number}]

(less-value 3 [:num1])              [{:selector [:num1], :type :vlad.validations/less-value, :vmax 3}]

(less-value 10 [:num1])             []

(greater-value 3 [:num1])           []

(greater-value 10 [:num1])          [{:selector [:num1], :type :vlad.validations/greater-value, :vmin 10}]

(greater-field [:num1] [:num4])     [{:first-selector [:num1], :second-selector [:num4], :type :vlad.validations/greater-field}]

(greater-field [:num4] [:num1])     []

(between-value 3 10 [:num1])        []

(between-value 8 10 [:num1])        [{:selector [:num1], :type :vlad.validations/greater-value, :vmin 8}]

(between-value 3 5 [:num1])         [{:selector [:num1], :type :vlad.validations/less-value, :vmax 5}]

(nil-value [:num1])                 [{:selector [:num1], :type :vlad.validations/nil-value}]

(nil-value [:num2])                 []

(nil-value [:num3])                 [{:selector [:num3], :type :vlad.validations/nil-value}]

(num-between 0 10 [:num2])          [{:selector [:num2], :type :vlad.validations/number}]

(num-between 0 10 [:num3])          [{:selector [:num3], :type :vlad.validations/number}]

(equal-or-greater-value 5 [:num1])  []

(equal-or-greater-value 7 [:num1])  []

(equal-or-greater-value 9 [:num1])  [{:selector [:num1], :type :vlad.validations/greater-value, :vmin 9}])

