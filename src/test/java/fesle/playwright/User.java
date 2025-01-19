package fesle.playwright;

/*
{
        "first_name": "John",
        "last_name": "Doe",
        "address": "Street 1",
        "city": "City",
        "state": "State",
        "country": "Country",
        "postcode": "1234AA",
        "phone": "0987654321",
        "dob": "1970-01-01",
        "password": "super-secret",
        "email": "john@doe.example"
        }
*/

import net.datafaker.Faker;

public record User(String first_name,
                   String last_name,
                   String address,
                   String city,
                   String state,
                   String country,
                   String postcode,
                   String phone,
                   String dob,
                   String password,
                   String email) {

    public static User randomUser() {
        Faker fake = new Faker();
        return new User(
                fake.name().firstName(),
                fake.name().lastName(),
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().postcode(),
                fake.phoneNumber().phoneNumber(),
                "1990-01-01",
                "Az123!&xyz",
                fake.internet().emailAddress()
        );
    }
}
