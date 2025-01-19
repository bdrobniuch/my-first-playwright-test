package fesle.playwright;

public class MockSearchResponses {
    public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
              "current_page": 1,
              "data": [
                {
                  "id": "01JHZ3GKPRW0KBHMZGQE5NHMJJ",
                  "name": "Super Pliers",
                  "description": "a really good pairs of pliers",
                  "price": 14.15,
                  "is_location_offer": false,
                  "is_rental": false,
                  "in_stock": true,
                  "product_image": {
                    "id": "01JHZ3GKP9ZNA5GPAX7M2TRSDE",
                    "by_name": "Helinton Fantin",
                    "by_url": "https://unsplash.com/@fantin",
                    "source_name": "Unsplash",
                    "source_url": "https://unsplash.com/photos/W8BNwvOvW4M",
                    "file_name": "pliers01.avif",
                    "title": "Super pliers"
                  }
                }
              ],
              "from": 1,
              "last_page": 1,
              "per_page": 9,
              "to": 1,
              "total": 1
            }
            """;

    public static final String  RESPONSE_WITH_NO_ENTRIES = """
                {
              "current_page": 1,
              "data": [],
              "from": 1,
              "last_page": 1,
              "per_page": 9,
              "to": 1,
              "total": 1
            }

""";
}
