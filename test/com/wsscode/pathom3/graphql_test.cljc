(ns com.wsscode.pathom3.graphql-test
  (:require
    [clojure.test :refer [deftest is are run-tests testing]]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.graphql :as p.gql]
    [com.wsscode.pathom3.interface.eql :as p.eql]))

(pci/register [])

(def query-root-type
  (p.gql/normalize-schema
    {:name   "QueryRoot"
     :fields [{:name "banks" :args [] :type {:kind "LIST" :name nil :ofType {:kind "OBJECT" :name "Bank"}}}
              {:name "creditCardAccount"
               :args [{:name "customerId" :defaultValue nil :type {:kind "SCALAR" :name "ID"}}]
               :type {:kind "OBJECT" :name "CreditCardAccount" :ofType nil}}
              {:name "customer"
               :args [{:name "customerId" :defaultValue nil :type {:kind "SCALAR" :name "ID"}}]
               :type {:kind :OBJECT :name "Customer" :ofType nil}}
              {:name "repository"
               :args [{:name "owner" :defaultValue nil :type {:kind "SCALAR" :name "String"}}
                      {:name "name" :defaultValue nil :type {:kind "SCALAR" :name "String"}}]
               :type {:kind "OBJECT" :name "Repository" :ofType nil}}
              {:name "savingsAccount"
               :args [{:name "customerId" :defaultValue nil :type {:kind "SCALAR" :name "ID"}}]
               :type {:kind "OBJECT" :name "SavingsAccount" :ofType nil}}
              {:name "viewer" :args [] :type {:kind "OBJECT" :name "Customer" :ofType nil}}]}))

(def bank-type
  {:name       "Bank"
   :kind       "OBJECT"
   :interfaces []
   :fields     [{:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "name" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def customer-type
  {:name       "Customer"
   :kind       "OBJECT"
   :interfaces []
   :fields     [{:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "cpf" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}
                {:name "creditCardAccount" :args [] :type {:kind "OBJECT" :name "CreditCardAccount" :ofType nil}}
                {:name "feed" :args [] :type {:kind "LIST" :name nil :ofType {:kind "NON_NULL" :name nil :ofType {:kind "INTERFACE" :name "FeedEvent"}}}}
                {:name "name" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}
                {:name "preferredName" :args [] :type {:kind "SCALAR" :name "String" :ofType nil}}
                {:name "savingsAccount" :args [] :type {:kind "OBJECT" :name "SavingsAccount" :ofType nil}}]})

(def credit-card-account-type
  {:name       "CreditCardAccount"
   :kind       "OBJECT"
   :interfaces []
   :fields     [{:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "number" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def savings-card-account-type
  {:name       "SavingsAccount"
   :kind       "OBJECT"
   :interfaces []
   :fields     [{:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "number" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def repository-type
  {:name       "Repository"
   :kind       "OBJECT"
   :interfaces []
   :fields     [{:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "name" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def feed-event-interface
  {:name       "FeedEvent"
   :kind       "INTERFACE"
   :interfaces []
   :fields     [{:name "detail" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}
                {:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "postDate" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "Date"}}}
                {:name "title" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def onboarding-event-type
  {:name       "OnboardingEvent"
   :kind       "OBJECT"
   :interfaces [{:name "FeedEvent" :kind "INTERFACE"}]
   :fields     [{:name "detail" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}
                {:name "id" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "ID"}}}
                {:name "postDate" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "Date"}}}
                {:name "title" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}}}]})

(def mutation-type
  {:name   "Mutation"
   :kind   "OBJECT"
   :fields [{:name "addStar"
             :args [{:name         "input"
                     :defaultValue nil
                     :type         {:kind   "NON_NULL"
                                    :name   nil
                                    :ofType {:kind   "INPUT_OBJECT"
                                             :name   "UserBlockProductInput"
                                             :ofType nil}}}]
             :type {:kind   "NON_NULL"
                    :name   nil
                    :ofType {:kind   "OBJECT"
                             :name   "Customer"
                             :ofType nil}}}
            {:name "removeStar"}
            {:name "requestReviews"}]})

(def types
  [{:name       "CreditCardBalances"
    :kind       "OBJECT"
    :interfaces []
    :fields     [{:name "available" :args [] :type {:kind "SCALAR" :name "Float" :ofType nil}}
                 {:name "due" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "Float"}}}
                 {:name "future" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "Float"}}}
                 {:name "open" :args [] :type {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "Float"}}}
                 {:name "prepaid" :args [] :type {:kind "SCALAR" :name "Float" :ofType nil}}]}
   query-root-type
   bank-type
   savings-card-account-type
   customer-type
   credit-card-account-type
   repository-type
   feed-event-interface
   onboarding-event-type
   mutation-type])

(def schema
  {:__schema
   {:queryType    {:name "QueryRoot"}
    :mutationType {:name "Mutation"}
    :types        types}})

(def prefix "service")
(def env {::p.gql/prefix "service" ::p.gql/mung identity})

(deftest test-type-key
  (is (= (p.gql/type-key env "CreditCardBalances")
         :service.types/CreditCardBalances)))

(deftest test-interface-key
  (is (= (p.gql/interface-key env "FeedEvent")
         :service.interfaces/FeedEvent)))

(deftest test-type->field-entry
  (is (= (p.gql/type->field-entry env {:kind "SCALAR" :name "Float" :ofType nil})
         {}))
  (is (= (p.gql/type->field-entry env {:kind "OBJECT" :name "CreditCardAccount" :ofType nil})
         {:service.types/CreditCardAccount {}}))
  (is (= (p.gql/type->field-entry env {:kind "INTERFACE" :name "FeedEvent" :ofType nil})
         {:service.interfaces/FeedEvent {}}))
  (is (= (p.gql/type->field-entry env {:kind "NON_NULL" :name nil :ofType {:kind "SCALAR" :name "String"}})
         {}))
  (is (= (p.gql/type->field-entry env {:kind "NON_NULL" :name nil :ofType {:kind "OBJECT" :name "CreditCardAccount" :ofType nil}})
         {:service.types/CreditCardAccount {}}))
  (is (= (p.gql/type->field-entry env {:kind "LIST" :name nil :ofType {:kind "OBJECT" :name "Bank"}})
         {:service.types/Bank {}})))

(deftest test-index-type
  (is (= (p.gql/index-type env customer-type)
         {#{:service.types/Customer} #:service.Customer{:cpf               {}
                                                        :creditCardAccount #:service.types{:CreditCardAccount {}}
                                                        :feed              #:service.interfaces{:FeedEvent {}}
                                                        :id                {}
                                                        :name              {}
                                                        :preferredName     {}
                                                        :savingsAccount    #:service.types{:SavingsAccount {}}}}))

  (is (= (p.gql/index-type env feed-event-interface)
         {#{:service.interfaces/FeedEvent} #:service.FeedEvent{:detail   {}
                                                               :id       {}
                                                               :postDate {}
                                                               :title    {}}}))

  (is (= (p.gql/index-type env onboarding-event-type)
         {#{:service.types/OnboardingEvent} {:service.OnboardingEvent/detail   {}
                                             :service.OnboardingEvent/id       {}
                                             :service.OnboardingEvent/postDate {}
                                             :service.OnboardingEvent/title    {}
                                             :service.interfaces/FeedEvent     {}}})))

(def supposed-resolver nil)

(def indexes
  `{:com.wsscode.pathom.connect.graphql2/field->ident {:service.Customer/cpf               #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/creditCardAccount #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/feed              #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/id                #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/name              #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/preferredName     #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Customer/savingsAccount    #:com.wsscode.pathom.connect.graphql2{:entity-field :service.Customer/id
                                                                                                                                 :ident-key    :customer/customerId}
                                                       :service.Repository/id              #:com.wsscode.pathom.connect.graphql2{:entity-field [:service.Customer/name
                                                                                                                                                :service.Repository/name]
                                                                                                                                 :ident-key    :repository/owner-and-name}
                                                       :service.Repository/name            #:com.wsscode.pathom.connect.graphql2{:entity-field [:service.Customer/name
                                                                                                                                                :service.Repository/name]
                                                                                                                                 :ident-key    :repository/owner-and-name}}
    :com.wsscode.pathom.connect/autocomplete-ignore   #{:service.interfaces/FeedEvent
                                                        :service.types/CreditCardBalances
                                                        :service.types/CreditCardAccount
                                                        :service.types/Customer
                                                        :service.types/Mutation
                                                        :service.types/OnboardingEvent
                                                        :service.types/Repository}
    :com.wsscode.pathom.connect/idents                #{:service.Customer/id}
    :com.wsscode.pathom.connect/index-io              {#{:service.Customer/id}              #:service.types{:Customer       {}
                                                                                                            :SavingsAccount {}}
                                                       #{:service.types/CreditCardAccount}  #:service.CreditCardAccount{:id     {}
                                                                                                                        :number {}}
                                                       #{:service.Customer/name
                                                         :service.Repository/name}          #:service.types{:Repository {}}
                                                       #{:service.interfaces/FeedEvent}     #:service.FeedEvent{:detail   {}
                                                                                                                :id       {}
                                                                                                                :postDate {}
                                                                                                                :title    {}}
                                                       #{:service.types/CreditCardBalances} #:service.CreditCardBalances{:available {}
                                                                                                                         :due       {}
                                                                                                                         :future    {}
                                                                                                                         :open      {}
                                                                                                                         :prepaid   {}}
                                                       #{:service.types/Customer}           #:service.Customer{:cpf               {}
                                                                                                               :creditCardAccount #:service.types{:CreditCardAccount {}}
                                                                                                               :feed              #:service.interfaces{:FeedEvent {}}
                                                                                                               :id                {}
                                                                                                               :name              {}
                                                                                                               :preferredName     {}
                                                                                                               :savingsAccount    #:service.types{:SavingsAccount {}}}
                                                       #{:service.types/Mutation}           #:service.Mutation{:addStar        #:service.types{:Customer {}}
                                                                                                               :removeStar     {}
                                                                                                               :requestReviews {}}
                                                       #{:service.types/OnboardingEvent}    {:service.OnboardingEvent/detail   {}
                                                                                             :service.OnboardingEvent/id       {}
                                                                                             :service.OnboardingEvent/postDate {}
                                                                                             :service.OnboardingEvent/title    {}
                                                                                             :service.interfaces/FeedEvent     {}}
                                                       #{:service.types/Repository}         #:service.Repository{:id   {}
                                                                                                                 :name {}}
                                                       #{}                                  #:service{:banks             #:service.types{:Bank {}}
                                                                                                      :creditCardAccount #:service.types{:CreditCardAccount {}}
                                                                                                      :customer          #:service.types{:Customer {}}
                                                                                                      :repository        #:service.types{:Repository {}}
                                                                                                      :savingsAccount    #:service.types{:SavingsAccount {}}
                                                                                                      :viewer            #:service.types{:Customer {}}}}
    :com.wsscode.pathom.connect/index-mutations       {com.wsscode.pathom.connect.graphql.service-mutations/service #:com.wsscode.pathom.connect{:sym com.wsscode.pathom.connect.graphql.service-mutations/service}
                                                       service/addStar                                              {:com.wsscode.pathom.connect.graphql2/output-type :service.types/Customer
                                                                                                                     :com.wsscode.pathom.connect/sym                  com.wsscode.pathom.connect.graphql.service-mutations/service}
                                                       service/removeStar                                           #:com.wsscode.pathom.connect{:sym com.wsscode.pathom.connect.graphql.service-mutations/service}
                                                       service/requestReviews                                       #:com.wsscode.pathom.connect{:sym com.wsscode.pathom.connect.graphql.service-mutations/service}}
    :com.wsscode.pathom.connect/index-oir             {:service.Customer/cpf               {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/creditCardAccount {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/feed              {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/id                {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/name              {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/preferredName     {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Customer/savingsAccount    {#{:service.Customer/id} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Repository/id              {#{:service.Customer/name
                                                                                              :service.Repository/name} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service.Repository/name            {#{:service.Customer/name
                                                                                              :service.Repository/name} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/banks                      {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/creditCardAccount          {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/customer                   {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/repository                 {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/savingsAccount             {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}
                                                       :service/viewer                     {#{} #{com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}}
    :com.wsscode.pathom.connect/index-resolvers       #:com.wsscode.pathom.connect.graphql2-test{supposed-resolver {:com.wsscode.pathom.connect.graphql2/graphql? true
                                                                                                                    :com.wsscode.pathom.connect/cache?            false
                                                                                                                    :com.wsscode.pathom.connect/dynamic-resolver? true
                                                                                                                    :com.wsscode.pathom.connect/sym               com.wsscode.pathom.connect.graphql2-test/supposed-resolver}}})

(deftest index-schema-io-test
  (p.gql/index-schema-io
    {::p.gql/prefix    prefix
     ::p.gql/schema    (p.gql/index-schema-types schema)
     ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                        "savingsAccount" {"customerId" :service.Customer/id}
                        "repository"     {"owner" :service.Customer/name
                                          "name"  :service.Repository/name}}
     ::p.gql/resolver  `supposed-resolver}))

(deftest index-aux-resolvers-test
  (p.gql/index-aux-resolvers
    {::p.gql/prefix    prefix
     ::p.gql/schema    (p.gql/index-schema-types schema)
     ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                        "savingsAccount" {"customerId" :service.Customer/id}
                        "repository"     {"owner" :service.Customer/name
                                          "name"  :service.Repository/name}}
     ::p.gql/resolver  `supposed-resolver}))

(comment
  (p.gql/index-schema-io
    {::p.gql/prefix    prefix
     ::p.gql/schema    (p.gql/index-schema-types schema)
     ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                        "savingsAccount" {"customerId" :service.Customer/id}
                        "repository"     {"owner" :service.Customer/name
                                          "name"  :service.Repository/name}}
     ::p.gql/resolver  `supposed-resolver})

  (let [env (p.gql/index-schema
              {::p.gql/prefix    prefix
               ::p.gql/schema    (p.gql/index-schema-types schema)
               ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                                  "savingsAccount" {"customerId" :service.Customer/id}
                                  "repository"     {"owner" :service.Customer/name
                                                    "name"  :service.Repository/name}}
               ::p.gql/resolver  `supposed-resolver})]
    (meta (p.eql/process env
       {:service.Customer/id 123}
       [:service.Customer/name
        :service.SavingsAccount/number])))

  (p.gql/index-schema
    {::p.gql/prefix    prefix
     ::p.gql/schema    (p.gql/index-schema-types schema)
     ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                        "savingsAccount" {"customerId" :service.Customer/id}
                        "repository"     {"owner" :service.Customer/name
                                          "name"  :service.Repository/name}}
     ::p.gql/resolver  `supposed-resolver})

  (p.gql/index-schema
    {::p.gql/prefix    prefix
     ::p.gql/schema    (p.gql/index-schema-types schema)
     ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                        "savingsAccount" {"customerId" :service.Customer/id}
                        "repository"     {"owner" :service.Customer/name
                                          "name"  :service.Repository/name}}
     ::p.gql/resolver  `supposed-resolver})

  (-> (p.gql/index-schema-types schema)
      :__schema))

#_(deftest test-index-schema
    (is (= (-> (p.gql/index-schema {::p.gql/prefix    prefix
                                    ::p.gql/schema    schema
                                    ::p.gql/ident-map {"customer"       {"customerId" :service.Customer/id}
                                                       "savingsAccount" {"customerId" :service.Customer/id}
                                                       "repository"     {"owner" :service.Customer/name
                                                                         "name"  :service.Repository/name}}
                                    ::p.gql/resolver  `supposed-resolver})
               (update-in [::pci/index-resolvers `supposed-resolver] dissoc ::pco/resolve)
               (update-in [::pci/index-mutations 'com.wsscode.pathom3.graphql.service-mutations/service] dissoc ::pco/mutate))
           indexes)))

;(deftest test-alias-for-line
;  (is (= (p.gql/alias-for-line "query { \ncustomer(customerId: \"123\") {\n}}" 2)
;         nil))
;
;  (is (= (p.gql/alias-for-line "query { \n_customer_customer_id_123: customer(customerId: \"123\") {\n}}" 2)
;         "_customer_customer_id_123"))
;
;  (is (= (p.gql/alias-for-line "query { \n_customer_customer_id_123: customer(customerId: \"123\") {\n}}" 10)
;         nil)))
;
;(deftest test-index-graphql-errors
;  (is (= (p.gql/index-graphql-errors
;           [{:message   "Parse error on \"-\" (error) at [3 11]"
;             :locations [{:line 3 :column 11}]}])
;         {nil [{:message   "Parse error on \"-\" (error) at [3 11]"
;                :locations [{:line 3 :column 11}]}]}))
;  (is (= (p.gql/index-graphql-errors
;           [{:message "Forbidden"
;             :path    ["didWrong"]}])
;         {["didWrong"] [{:message "Forbidden", :path ["didWrong"]}]}))
;  (is (= (p.gql/index-graphql-errors
;           [{:message "Forbidden"
;             :path    ["query" "didWrong"]}])
;         {["didWrong"] [{:message "Forbidden", :path ["didWrong"]}]}))
;  (is (= (p.gql/index-graphql-errors
;           [{:path       ["mutation" "addStar" "clientMutation"]
;             :extensions {:code      "undefinedField"
;                          :typeName  "AddStarPayload"
;                          :fieldName "clientMutation"}
;             :locations  [{:line 3 :column 5}]
;             :message    "Field 'clientMutation' doesn't exist on type 'AddStarPayload'"}])
;         {["addStar" "clientMutation"] [{:path       ["addStar" "clientMutation"]
;                                         :extensions {:code      "undefinedField"
;                                                      :typeName  "AddStarPayload"
;                                                      :fieldName "clientMutation"}
;                                         :locations  [{:line 3 :column 5}]
;                                         :message    "Field 'clientMutation' doesn't exist on type 'AddStarPayload'"}]})))
;
;(deftest test-parse-item
;  (is (= (p.gql/parser-item {::p/entity {}} [])
;         {}))
;  (is (= (p.gql/parser-item {::p/entity     {:itemValue 42}
;                             ::p.gql/demung pg/camel-case}
;           [:ns/item-value])
;         {:ns/item-value 42}))
;  (is (= (p.gql/parser-item {::p/entity               {:itemValue {:x 1 :y 2}}
;                             ::p/placeholder-prefixes #{">"}}
;           [{:itemValue [:x {:>/sub [:y]}]}])
;         {:itemValue {:x 1 :>/sub {:y 2}}}))
;  (is (= (p.gql/parser-item {::p/entity     {:didWrong nil}
;                             ::p.gql/demung pg/camel-case
;                             ::p.gql/errors (p.gql/index-graphql-errors
;                                              [{:message "Forbidden"
;                                                :path    ["didWrong"]}])}
;           [{:did-wrong [:anything]}])
;         {:did-wrong ::p/reader-error}))
;  (testing "capture error"
;    (let [errors* (atom {})]
;      (is (= (p.gql/parser-item {::p/entity            {:_customer_customer_id_123 {:creditCardAccount nil}}
;                                 ::p/errors*           errors*
;                                 ::p.gql/demung        pg/camel-case
;                                 ::p.gql/base-path     [[:service.Customer/id "123"]]
;                                 ::p.gql/graphql-query "query \n{_customer_customer_id_123: customer(customerId: \"123\") \n{}}"
;                                 ::p.gql/errors        (p.gql/index-graphql-errors [{:locations [{:column 123 :line 2}]
;                                                                                     :message   "Forbidden"
;                                                                                     :path      ["customer" "creditCardAccount"]
;                                                                                     :type      "forbidden"}])}
;               [{[:customer/customerId "123"] [{:service.Customer/credit-card-account [:service.credit-card-balances/available]}]}])
;             {[:customer/customerId "123"] {:service.Customer/credit-card-account ::p/reader-error}}))
;      (is (= @errors*
;             {[[:service.Customer/id "123"] :service.Customer/credit-card-account] {:locations [{:column 123 :line 2}]
;                                                                                    :message   "Forbidden"
;                                                                                    :path      ["customer" "creditCardAccount"]
;                                                                                    :type      "forbidden"}}))))
;
;  (testing "mutation errors"
;    {:errors
;     [{:path      ["query" "nameWithOwneree"],
;       :extensions
;                  {:code      "undefinedField",
;                   :typeName  "Query",
;                   :fieldName "nameWithOwneree"},
;       :locations [{:line 7, :column 3}],
;       :message
;                  "Field 'nameWithOwneree' doesn't exist on type 'Query'"}]}
;
;    {:errors
;     [{:message   "Parse error on \"-\" (error) at [3 11]"
;       :locations [{:line 3 :column 11}]}]}))
;
;; TODO proper process mutation error responses
;
;(comment
;  (let [errors* (atom {})]
;    [(p.gql/parser-item {::p/entity            nil
;                         ::p/errors*           errors*
;                         ::p.gql/base-path     []
;                         ::p.gql/graphql-query "query {\n  addStar(input: {starrableId: \"MDEwOlJlcG9zaXRvcnk5ODU5MDk2MQ==\"}) {\n    clientMutation\n    starrable {\n      viewerHasStarred\n    }\n  }\n}"
;                         ::p.gql/errors        (p.gql/index-graphql-errors [{:path       ["mutation" "addStar" "clientMutation"]
;                                                                             :extensions {:code      "undefinedField"
;                                                                                          :typeName  "AddStarPayload"
;                                                                                          :fieldName "clientMutation"}
;                                                                             :locations  [{:line 3 :column 5}]
;                                                                             :message    "Field 'clientMutation' doesn't exist on type 'AddStarPayload'"}])}
;       '[{(:github/addStar
;            {:github/input
;             {:github/starrableId "MDEwOlJlcG9zaXRvcnk5ODU5MDk2MQ=="}})
;          [:clientMutation {:starrable [:viewerHasStarred]}]}])
;     @errors*])
;
;  (let [errors* (atom {})]
;    [(p.gql/parser-item {::p/entity            nil
;                         ::p/errors*           errors*
;                         ::p.gql/base-path     []
;                         ::p.gql/graphql-query "query {\n  addStar(input: {starrableId: \"MDEwOlJlcG9zaXRvcnk5ODU5MDk2MQ==\"}) {\n    clientMutation\n    starrable {\n      viewerHasStarred\n    }\n  }\n}"
;                         ::p.gql/errors        (p.gql/index-graphql-errors
;                                                 [{:message   "Parse error on \"-\" (error) at [3 11]"
;                                                   :locations [{:line 3 :column 11}]}])}
;       '[{(:github/addStar
;            {:github/input
;             {:github/starrableId "MDEwOlJlcG9zaXRvcnk5ODU5MDk2MQ=="}})
;          [:clientMutation {:starrable [:viewerHasStarred]}]}])
;     @errors*]))
;
;(comment
;  (println
;    (p.gql/query->graphql '[{(:github/addStar
;                               {:github/input
;                                {:github/starrableId "MDEwOlJlcG9zaXRvcnk5ODU5MDk2MQ=="}})
;                             [:clientMutation {:starrable [:viewerHasStarred]}]}]
;      {})))
;
;(defn- normalize-query-whitespace [s]
;  (str/trim (str/replace s #"\s+" " ")))
;
;(deftest test-query->graphql
;  (are [query out] (= (normalize-query-whitespace query) out)
;
;    (p.gql/query->graphql [{:credit-card [:number]}] {::p.gql/demung pg/camel-case})
;    "query { creditCard { number } }"
;
;    (p.gql/query->graphql [(list 'call {:id (fp/tempid) :param "value"})] {::p.gql/tempid? fp/tempid?})
;    "mutation { call(param: \"value\") { id} }"))
;
;(defn q [query]
;  (p/query->ast1 [query]))
;
;(deftest test-ast->graphql
;  (is (= (p.gql/ast->graphql {:ast         (q :service/banks)
;                              ::pc/indexes indexes} {})
;         [:service/banks]))
;  (is (= (p.gql/ast->graphql {:ast         (q {:service/banks [:service.Bank/name]})
;                              ::pc/indexes indexes} {})
;         [{:service/banks [:service.Bank/name]}]))
;  (is (= (p.gql/ast->graphql {:ast         (q :service.Customer/cpf)
;                              ::pc/indexes indexes}
;           {:service.Customer/id "123"})
;         [{[:customer/customerId
;            "123"] [:service.Customer/cpf]}])))
;
;(defn query-env [query-attribute entity]
;  {:ast                     (q query-attribute)
;   ::p/entity               entity
;   ::p/placeholder-prefixes #{">"}
;   ::p/parent-query         [query-attribute]
;   ::p.gql/prefix           prefix
;   ::pc/indexes             indexes})
;
;(deftest test-build-query
;  (testing "build global attribute"
;    (is (= (p.gql/build-query (query-env :service/banks
;                                {:service.Customer/id "123"}))
;           [:service/banks])))
;
;  (testing "remove pathom params"
;    (is (= (p.gql/build-query (query-env '(:service/banks {:pathom/as :banks})
;                                {:service.Customer/id "123"}))
;           ['(:service/banks)])))
;
;  (testing "ident join"
;    (is (= (p.gql/build-query (query-env :service.Customer/cpf
;                                {:service.Customer/id "123"}))
;           [{[:customer/customerId "123"] [:service.Customer/cpf]}])))
;
;  (testing "ident join on multi param input"
;    (is (= (p.gql/build-query (query-env :service.Repository/id
;                                {:service.Customer/name   "customer"
;                                 :service.Repository/name "repository"}))
;           [{[:repository/owner-and-name ["customer" "repository"]] [:service.Repository/id]}])))
;
;  (testing "ignores ident queries"
;    (is (= (p.gql/build-query (query-env {[:service.Customer/id "123"] [:service.Customer/name]}
;                                {:service.Customer/id "123"}))
;           [])))
;
;  (testing "merge sibling queries"
;    (is (= (p.gql/build-query (assoc (query-env :service.Customer/id {:service.Customer/id "123"})
;                                ::p/parent-query [:service.Customer/id
;                                                  :service.Customer/cpf
;                                                  :service/banks
;                                                  :service.Customer/name
;                                                  :other/thing]))
;           [{[:customer/customerId "123"] [:service.Customer/cpf :service.Customer/name]}
;            :service/banks])))
;
;  (testing "placeholder queries"
;    (is (= (p.gql/build-query (assoc (query-env :service.Customer/id {:service.Customer/id "123"})
;                                ::p/parent-query [:service.Customer/id
;                                                  {:>/thing [:service.Customer/cpf]}
;                                                  :service/banks
;                                                  :service.Customer/name
;                                                  :other/thing]))
;           [:service/banks
;            {[:customer/customerId "123"] [:service.Customer/name :service.Customer/cpf]}]))))
;
;(deftest test-pull-idents
;  (is (= (p.gql/pull-idents {:service/banks               [{:service.Bank/name "Dino"}]
;                             [:customer/customerId "123"] {:service.Customer/name "Missy"}})
;         {:service/banks         [{:service.Bank/name "Dino"}]
;          :service.Customer/name "Missy"})))
