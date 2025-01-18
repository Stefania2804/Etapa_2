package org.poo.main;

public enum Constants {
    FOODLIMIT(2),
    CLOTHESLIMIT(5),
    TECHLIMIT(10),
    CASHBACK2(0.02),
    CASHBACK5(0.05),
    CASHBACK10(0.1),
    CASHBACK01(0.001),
    CASHBACK03(0.003),
    CASHBACK05(0.005),
    CASHBACK02(0.002),
    CASHBACK04(0.004),
    CASHBACK055(0.0055),
    CASHBACK025(0.0025),
    CASHBACK07(0.007),
    COMISION02(0.002),
    COMISION01(0.001),
    FIRSTTHRESHOLD(100),
    SECONDTHRESHOLD(300),
    THIRDTHRESHOLD(500),
    COMISIONLIMIT(500),
    DEPOSITLIMIT(500),
    SPENDINGLIMIT(500),
    MINIMSPENT(300),
    UPGRADESILVER(100),
    UPGRADEGOLD1(350),
    UPGRADEGOLD2(250);

    private final double value;

    Constants(final double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
