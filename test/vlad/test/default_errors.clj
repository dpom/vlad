(ns vlad.test.default-errors
  (:use vlad.default-errors
        midje.sweet)
  (:require [vlad.validation-types :as vt]
            [vlad.validations :as v]))

(tabular
  (fact (translate-errors ?errors english-translation) => ?translations)
  ?errors ?translations

  [{:type :vlad.validations/present :selector [:password] :name "Password"}]
  {[:password] ["Password is required."]}

  [{:type :vlad.validations/length-over :selector [:password] :name "Password" :size 8}]
  {[:password] ["Password must be over 8 characters long."]}

  [{:type :vlad.validations/one-of :selector [:title] :name "Title" :set #{"Mr" "Ms" "Mrs"}}]
  {[:title] ["Title must be one of Mr, Ms, Mrs."]}

  [{:type :vlad.validations/not-of :selector [:username] :name "Username" :set #{"login" "logout"}}]
  {[:username] ["Username must not be one of login, logout."]}

  [{:type :vlad.validations/equals-value :selector [:over_18] :name "Over 18" :value "yes"}]
  {[:over_18] ["Over 18 must be \"yes\"."]}

  [{:type :vlad.validations/equals-field
    :first-name "Password" :first-selector [:password]
    :second-name "Password confirmation" :second-selector [:password-confirmation]}]
  {nil ["Password must be the same as Password confirmation."]}

  [{:type :vlad.validations/matches :name "Username" :selector [:username] :pattern #"\w+"}]
  {[:username] ["Username must match the pattern \\w+."]})

(fact (assign-name (vt/validate (v/present [:foo]) {})
             {[:foo] "Foozle"})
      => [{:first-name nil, :name "Foozle", :second-name nil, :selector [:foo], :type :vlad.validations/present}])

(fact "about assign-name with 2 selectors"
  (assign-name (vt/validate (v/greater-field [:num1] [:num2]) {:num1 3 :num2 9}) {[:num1] "Num1" [:num2] "Num2"})
  => [{:first-name "Num1", :first-selector [:num1], :name nil, :second-name "Num2", :second-selector [:num2], :type :vlad.validations/greater-field}])
