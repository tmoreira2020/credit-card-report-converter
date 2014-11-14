/**
 * Copyright (C) 2012 Thiago Moreira (tmoreira2020@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.thiagomoreira.bancodobrasil;
import java.math.BigDecimal;
import java.text.NumberFormat;

import net.sf.ofx4j.io.DefaultStringConversion;


public class MyFinanceStringConversion extends DefaultStringConversion {

	@Override
	public String toString(Object value) {
		if (value instanceof BigDecimal) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(2);
			return numberFormat.format(value);
		}
		return super.toString(value);
	}
}
