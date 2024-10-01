package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    ru.netology.pages.DashboardPage dashboardPage;

    @BeforeEach
    void setup(){
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }


    @Test
    void transferOfTheFirstCard() {
        int amount = 2000;
        var cardBalanceFirst = dashboardPage.getCardBalance("01");
        var cardBalanceSecond = dashboardPage.getCardBalance("02");
        var cardInfo = DataHelper.getSecondCardInfo();
        var transferMoney = dashboardPage.firstCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceAfterSendFirst = DataHelper.cardBalanceAfterGetMoney(cardBalanceFirst, amount);
        var cardBalanceAfterSendSecond = DataHelper.cardBalanceAfterSendMoney(cardBalanceSecond, amount);
        assertEquals(cardBalanceAfterSendFirst, dashboardPage.getCardBalance("01"));
        assertEquals(cardBalanceAfterSendSecond, dashboardPage.getCardBalance("02"));
    }

    @Test
    void transferOfTheSecondCard() {
        int amount = 1000;
        var cardBalanceFirst = dashboardPage.getCardBalance("01");
        var cardBalanceSecond = dashboardPage.getCardBalance("02");
        var cardInfo = DataHelper.getFirstCardInfo();
        var transferMoney = dashboardPage.secondCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceAfterSendFirst = DataHelper.cardBalanceAfterSendMoney(cardBalanceFirst, amount);
        var cardBalanceAfterSendSecond = DataHelper.cardBalanceAfterGetMoney(cardBalanceSecond, amount);
        assertEquals(cardBalanceAfterSendFirst, dashboardPage.getCardBalance("01"));
        assertEquals(cardBalanceAfterSendSecond, dashboardPage.getCardBalance("02"));
    }

    @Test
    void transferFromCardOneToCardOne() {
        int amount = 6000;
        var cardBalanceFirstBefore = dashboardPage.getCardBalance("01");
        var cardBalanceSecond = dashboardPage.getCardBalance("02");
        var cardInfo = DataHelper.getFirstCardInfo();
        var transferMoney = dashboardPage.firstCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceFirstAfter = dashboardPage.getCardBalance("01");
        assertEquals(cardBalanceFirstBefore, cardBalanceFirstAfter);

    }
    @Test
    void transferFromCardTwoToCardTwo() {
        int amount = 6000;
        var cardBalanceFirst = dashboardPage.getCardBalance("01");
        var cardBalanceSecondBefore = dashboardPage.getCardBalance("02");
        var cardInfo = DataHelper.getFirstCardInfo();
        var transferMoney = dashboardPage.firstCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        var cardBalanceSecondAfter = dashboardPage.getCardBalance("02");
        assertEquals(cardBalanceSecondBefore, cardBalanceSecondAfter);
    }
    @Test
    void NotTransferMoneyFromSecondToFirst() {
        int amount = 15000;
        var cardBalanceFirst = dashboardPage.getCardBalance("01");
        var cardBalanceSecond = dashboardPage.getCardBalance("02");
        var cardInfo = DataHelper.getFirstCardInfo();
        var transferMoney = dashboardPage.secondCardButtonClick();
        transferMoney.transfer(cardInfo, amount);
        transferMoney.showAlertMessage("Ошибка! Недостаточно средств на счете!");
    }
}