(ns vlad.test.validations
  (:use [midje.sweet :only [tabular fact just contains]]
        [vlad validations validation_types]))

(tabular
  (fact (validate ?validator {:name "Chris" :confirm_name "Brad"}) => ?errors)
  ?validator ?errors

  (present "Name" [:name])
  []                   

  (present "Age"  [:age])
  [{:type :vlad.validations/present
    :name "Age"
    :selector [:age]}] 

  (length-over 4 "Name" [:name])
  []                   

  (length-over 9 "Name" [:name])
  [{:type :vlad.validations/length-over
    :size 9
    :name "Name"
    :selector [:name]}] 

  (length-under 9 "Name" [:name])
  []                   

  (length-under 4 "Name" [:name])
  [{:type :vlad.validations/length-under
    :size 4
    :name "Name"
    :selector [:name]}] 

  (length-in 4 9 "Name" [:name])
  []                   

  (length-in 9 4 "Name" [:name])
  [{:type :vlad.validations/length-over
    :size 9
    :name "Name"
    :selector [:name]}
   {:type :vlad.validations/length-under
    :size 4
    :name "Name"
    :selector [:name]}]

  (one-of #{"Chris" "Fred"} "Name" [:name])
  []

  (one-of #{"Thelma" "Luise"} "Name" [:name])
  [{:type :vlad.validations/one-of
    :set #{"Thelma" "Luise"}
    :name "Name"
    :selector [:name]}]

  (not-of #{"Thelma" "Luise"} "Name" [:name])
  []

  (not-of #{"Chris" "Fred"} "Name" [:name])
  [{:type :vlad.validations/not-of
    :set #{"Chris" "Fred"}
    :name "Name"
    :selector [:name]}]

  (equals-value "Chris" "Name" [:name])
  []

  (equals-value "Maddy" "Name" [:name])
  [{:type :vlad.validations/equals-value
    :value "Maddy"
    :name "Name"
    :selector [:name]}] 

  (equals-field "Name" [:name] "Name confirmation" [:name])
  []

  (equals-field "Name" [:name] "Name confirmation" [:confirm_name])
  [{:type :vlad.validations/equals-field
    :first-name "Name"
    :first-selector [:name]
    :second-name "Name confirmation"
    :second-selector [:confirm_name]}]
  
  (matches #"..ris" "Name" [:name])
  []
  
  (matches #"andy" "Name" [:name])
  (just [(contains {:type :vlad.validations/matches
                              :name "Name"
                              :selector [:name]})]))
