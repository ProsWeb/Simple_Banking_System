package banking;

public class Checker {

    private Integer sumOfDigits = 0;
    private Integer checkSum = 0;

    public Integer getCheckSum(final String numWithoutCheckSum) {

        generate(numWithoutCheckSum);
        return checkSum;
    }

    public Integer getSumOfDigits(final String numWithoutCheckSum) {

        generate(numWithoutCheckSum);
        return sumOfDigits;
    }

    private void generate(final String numWithoutCheckSum) {

        for (int i = 0; i < numWithoutCheckSum.length(); i++) {
            if (i % 2 == 0) {
                int doubledNum = Integer
                        .parseInt(numWithoutCheckSum.substring(i, i + 1)) * 2;
                if (doubledNum > 9) {
                    this.sumOfDigits += doubledNum - 9;
                } else {
                    this.sumOfDigits += doubledNum;
                }
            } else {
                String num = numWithoutCheckSum.substring(i, i + 1);
                this.sumOfDigits += Integer.parseInt(num);
            }
        }

        while ((this.sumOfDigits + this.checkSum) % 10 != 0) {
            this.checkSum += 1;
        }
    }
}
