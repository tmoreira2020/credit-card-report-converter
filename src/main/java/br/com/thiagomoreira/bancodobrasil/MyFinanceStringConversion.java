package br.com.thiagomoreira.bancodobrasil;
import java.text.NumberFormat;

import net.sf.ofx4j.io.DefaultStringConversion;


public class MyFinanceStringConversion extends DefaultStringConversion {

	@Override
	public String toString(Object value) {
		if (value instanceof Double) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(2);
			return numberFormat.format(value);
		}
		return super.toString(value);
	}
}
