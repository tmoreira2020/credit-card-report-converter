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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.ofx4j.domain.data.common.Currency;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.common.TransactionType;

public class DefaultParser extends AbstractParser {

	protected NumberFormat formatter = NumberFormat
			.getNumberInstance(new Locale("pt", "BR"));
	protected DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

	public List<Transaction> parse(List<String> lines) throws Exception {
		List<Transaction> transactions = new ArrayList<Transaction>();

		for (String line : lines) {
			if (line.trim().length() != 0) {
				Pattern pattern = Pattern
						.compile("(\\d\\d/\\d\\d)    (.{38}) ((\\w|\\s){3}) ((\\s)*-?(\\.|\\d)*,\\d\\d) ((\\s)*-?(\\.|\\d)*,\\d\\d)");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					Date date = dateFormat.parse(matcher.group(1) + "/16");
					String description = matcher.group(2);
					String amountInReals = matcher.group(5).trim();
					String amountInDollars = matcher.group(8).trim();
					String id = String.valueOf(Math
							.abs((amountInReals + description).hashCode()));

					if (description.contains("PGTO DEBITO CONTA")
							|| description.contains("PGTO. CASH AG.")) {
						continue;
					}

					if (startDate == null) {
						startDate = date;
						endDate = date;
					}

					if (endDate.before(date)) {
						endDate = date;
					}

					Transaction transaction = new Transaction();

					transaction.setTransactionType(TransactionType.OTHER);
					transaction.setDatePosted(date);
					transaction.setId(id);
					transaction.setCheckNumber(id);
					transaction.setReferenceNumber(id);
					double amount = formatter.parse(amountInReals)
							.doubleValue();
					brazilianRealsAmount = brazilianRealsAmount + amount;

					if (amount == 0) {
						amount = formatter.parse(amountInDollars).doubleValue();
						transaction.setCurrency(new Currency());
						dolarsAmount = dolarsAmount + amount;
					}
					transaction.setAmount(-1 * amount);
					transaction.setMemo(description);

					transactions.add(transaction);
				}
				pattern = Pattern.compile("X\\s*(\\d+(\\.|,)\\d\\d\\d?\\d?)");
				matcher = pattern.matcher(line);
				while (matcher.find()) {
					exchangeRate = Float.parseFloat(matcher.group(1).replace(
							',', '.'));
				}
				pattern = Pattern.compile("Modalidade      : (.{30})");
				matcher = pattern.matcher(line);
				while (matcher.find()) {
					accountKey = matcher.group(1).trim();
				}
			}
		}

		for (Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency();
			if (currency != null) {
				transaction.setCurrency(null);
				double temp = exchangeRate * transaction.getAmount();
				transaction.setAmount(temp);
			}
		}

		return transactions;
	}
}
