package generators;

import models.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerators {

    public static User generateUser() {
        String raw = "user_" + RandomStringUtils.randomAlphanumeric(6) + "@yandex.ru";
        String email = raw.toLowerCase();
        return User.builder()
                .email(email)
                .password(RandomStringUtils.randomAlphanumeric(8))
                .name(RandomStringUtils.randomAlphabetic(6))
                .build();
    }

}
