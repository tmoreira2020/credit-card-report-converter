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
