package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @Test
    void TransferOfTheFirstCard() {
        int amount = 111 + (int) (Math.random() * 5000);
        var authInfo = DataHelper.getAuthInfo();
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        var dashboard = verificationPage.validVerify(verificationCode);
        var cardBalanceFirst = dashboard.getCardBalance("01");
        var cardBalanceSecond = dashboard.getCardBalance("02");
        var cardInfo = DataHelper.Card.getSecondCardInfo();
        var transferMoney = dashboard.firstCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceAfterSendFirst = DataHelper.Card.cardBalanceAfterGetMoney(cardBalanceFirst, amount);
        var cardBalanceAfterSendSecond = DataHelper.Card.cardBalanceAfterSendMoney(cardBalanceSecond, amount);
        assertEquals(cardBalanceAfterSendFirst, dashboard.getCardBalance("01"));
        assertEquals(cardBalanceAfterSendSecond, dashboard.getCardBalance("02"));
    }

    @Test
    void TransferOfTheSecondCard() {
        int amount = 111 + (int) (Math.random() * 5000);
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        var dashboard = verificationPage.validVerify(verificationCode);
        var cardBalanceFirst = dashboard.getCardBalance("01");
        var cardBalanceSecond = dashboard.getCardBalance("02");
        var cardInfo = DataHelper.Card.getFirstCardInfo();
        var transferMoney = dashboard.secondCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceAfterSendFirst = DataHelper.Card.cardBalanceAfterSendMoney(cardBalanceFirst, amount);
        var cardBalanceAfterSendSecond = DataHelper.Card.cardBalanceAfterGetMoney(cardBalanceSecond, amount);
        assertEquals(cardBalanceAfterSendFirst, dashboard.getCardBalance("01"));
        assertEquals(cardBalanceAfterSendSecond, dashboard.getCardBalance("02"));
    }

    @Test
    void transferFromCardOneToCardOne() {
        int amount = 6000;
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        var dashboard = verificationPage.validVerify(verificationCode);
        var cardBalanceFirst = dashboard.getCardBalance("01");
        var cardBalanceSecond = dashboard.getCardBalance("02");
        var cardInfo = DataHelper.Card.getFirstCardInfo();
        var transferMoney = dashboard.firstCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        transferMoney.showAlertMessage("Ошибка! Вы не можете совершить операцию по данной карте. Пожалуйста, выберите другую карту или счет зачисления!");
    }


    @Test
    void NotTransferMoneyFromSecondToFirst() {
        int amount = 15000;
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        var dashboard = verificationPage.validVerify(verificationCode);
        var cardBalanceFirst = dashboard.getCardBalance("01");
        var cardBalanceSecond = dashboard.getCardBalance("02");
        var cardInfo = DataHelper.Card.getFirstCardInfo();
        var transferMoney = dashboard.secondCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        transferMoney.showAlertMessage("Ошибка! Недостаточно средств на счете!");
    }
}