Feature: Adding Non Perishable Stock

    Background:
        Given there are no articles nor stocks

    Scenario: Admin adds a stock
        Given non perishable article "Stylo" exists
        When Admin adds a new stock with of 10 articles "Stylo"
        Then A stock of 10 articles "Stylo" is created

    Scenario: Admin adds a negative quantity of a stock
        Given non perishable article "Stylo" exists
        When Admin adds a new stock with of -10 articles "Stylo"
        Then there is an error

    Scenario: Admin adds a stock of a perishable article
        Given perishable article "Stylo" exists
        When Admin adds a new stock with of 10 articles "Stylo"
        Then there is an error

    Scenario: Admin adds a stock, but the article does not exist
        Given no article exist
        When Admin adds a new stock with of 10 articles "Stylo"
        Then there is an error
