package br.com.thiagomoreira.bancodobrasil;
/**
 * Copyright (c) 2012 Thiago Leão Moreira. All rights reserved.
 *
 * Licensed under the LGPL License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import net.sf.ofx4j.domain.data.ApplicationSecurity;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Status;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.common.TransactionList;
import net.sf.ofx4j.domain.data.creditcard.CreditCardAccountDetails;
import net.sf.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet;
import net.sf.ofx4j.domain.data.creditcard.CreditCardStatementResponse;
import net.sf.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction;
import net.sf.ofx4j.io.AggregateMarshaller;
import net.sf.ofx4j.io.v1.OFXV1Writer;

import org.apache.commons.io.IOUtils;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args != null) {
			double total = 0;
			for (String arg : args) {
				File input = new File(arg);
				if (input.exists()) {
					List<String> lines = IOUtils.readLines(new FileInputStream(input), "ISO-8859-1");

					Parser parser = new DefaultParser();

					List<Transaction> transactions = parser.parse(lines);

					TransactionList transactionList = new TransactionList();
					transactionList.setStart(parser.getStartDate());
					transactionList.setEnd(parser.getEndDate());
					transactionList.setTransactions(transactions);

					CreditCardAccountDetails creditCardAccountDetails = new CreditCardAccountDetails();
					creditCardAccountDetails.setAccountNumber("7616-3");
					creditCardAccountDetails.setAccountKey(parser.getAccountKey());
							
					CreditCardStatementResponse creditCardStatementResponse = new CreditCardStatementResponse();
					creditCardStatementResponse.setAccount(creditCardAccountDetails);
					creditCardStatementResponse.setCurrencyCode("BRL");
					creditCardStatementResponse.setTransactionList(transactionList);

					Status status = new Status();
					status.setCode(Status.KnownCode.SUCCESS);
					status.setSeverity(Status.Severity.INFO);

					CreditCardStatementResponseTransaction statementResponse = new CreditCardStatementResponseTransaction();
					statementResponse.setClientCookie(UUID.randomUUID().toString());
					statementResponse.setStatus(status);
					statementResponse.setUID(UUID.randomUUID().toString());
					statementResponse.setMessage(creditCardStatementResponse);

					CreditCardResponseMessageSet creditCardResponseMessageSet = new CreditCardResponseMessageSet();
					creditCardResponseMessageSet.setStatementResponse(statementResponse);

					SortedSet<ResponseMessageSet> messageSets = new TreeSet<ResponseMessageSet>();
					messageSets.add(creditCardResponseMessageSet);

					ResponseEnvelope envelope = new ResponseEnvelope();
					envelope.setUID(UUID.randomUUID().toString());
					envelope.setSecurity(ApplicationSecurity.NONE);
					envelope.setMessageSets(messageSets);

					double brazilianRealsamount = parser.getBrazilianRealsAmount();
					double dolarsAmount = parser.getDolarsAmount();
					double cardTotal = dolarsAmount*parser.getExchangeRate() + brazilianRealsamount;
					total+= cardTotal;

					System.out.println(creditCardAccountDetails.getAccountKey());
					System.out.println("TOTAL EM RS " + brazilianRealsamount);
					System.out.println("TOTAL EM US " + dolarsAmount);
					System.out.println("TOTAL FATURA EM RS " + cardTotal);
					System.out.println();

					if (!transactions.isEmpty()) {
						String parent = System.getProperty("user.home") + "/Downloads";
						String fileName = arg.replace(".txt", ".ofx");
						File output = new File(parent, fileName);
						FileOutputStream fos = new FileOutputStream(output);

						OFXV1Writer writer = new OFXV1Writer(fos);
						writer.setWriteAttributesOnNewLine(true);

						AggregateMarshaller marshaller = new AggregateMarshaller();
						marshaller.setConversion(new MyFinanceStringConversion());
						marshaller.marshal(envelope, writer);

						writer.flush();
						writer.close();
					}
				}
			}
			System.out.println("TOTAL FATURAS EM RS " + total);
		}

	}

}
