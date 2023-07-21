package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
//        Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id= 'city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(firstMeetingDate);
        $("[data-test-id= 'name'] input").setValue(validUser.getName());
        $("[data-test-id= 'phone'] input").setValue(validUser.getPhone());
        $("[data-test-id= 'agreement']").click();
        $$("[type= 'button']").find(exactText("Запланировать")).click();
        $(".notification__content").shouldHave(Condition.text(
                        "Встреча успешно запланирована на " + firstMeetingDate),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

        $$("[type= 'button']").filter(Condition.visible).first().click();
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(secondMeetingDate);
        $$("[type= 'button']").find(Condition.text("Запланировать")).click();
        $("[data-test-id= 'replan-notification']").shouldBe(Condition.visible).shouldHave(Condition.text(
                        "У вас уже запланирована встреча на другую дату. Перепланировать?"),
                Duration.ofSeconds(15));

        $$("[type= 'button']").find(Condition.text("Перепланировать")).click();
        $(".notification__content").shouldHave(exactText(
                        "Встреча успешно запланирована на " + secondMeetingDate),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}