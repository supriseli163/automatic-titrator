package com.jh.automatic_titrator.ui.chat.formatter;

import java.text.DecimalFormat;

public class DefaultValueFormatter extends ValueFormatter
{

    /**
     * DecimalFormat for formatting
     */
    protected DecimalFormat mFormat;

    protected int mDecimalDigits;

    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DefaultValueFormatter(int digits) {
        setup(digits);
    }

    /**
     * Sets up the formatter with a given number of decimal digits.
     *
     * @param digits
     */
    public void setup(int digits) {

        this.mDecimalDigits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value) {

        // put more logic here ...
        // avoid memory allocations here (for performance reasons)

        return mFormat.format(value);
    }

    /**
     * Returns the number of decimal digits this formatter uses.
     *
     * @return
     */
    public int getDecimalDigits() {
        return mDecimalDigits;
    }
}