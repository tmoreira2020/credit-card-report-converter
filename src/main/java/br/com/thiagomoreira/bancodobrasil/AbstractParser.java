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
import java.util.Date;

public abstract class AbstractParser implements Parser {
	protected float exchangeRate = 0;
	protected String accountKey = null;
	protected Date startDate = null;
	protected Date endDate = null;
	protected double brazilianRealsAmount = 0;
	protected double dolarsAmount = 0;

	public double getBrazilianRealsAmount() {
		return brazilianRealsAmount;
	}

	public double getDolarsAmount() {
		return dolarsAmount;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public Date getEndDate() {
		return endDate;
	}

	public float getExchangeRate() {
		return exchangeRate;
	}

	public Date getStartDate() {
		return startDate;
	}

}
