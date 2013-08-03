import java.util.Date;
import java.util.List;

import net.sf.ofx4j.domain.data.common.Transaction;

public interface Parser {

	public abstract String getAccountKey();

	public abstract double getBrazilianRealsAmount();

	public abstract double getDolarsAmount();

	public abstract Date getEndDate();

	public abstract float getExchangeRate();
	
	public abstract Date getStartDate();

	public abstract List<Transaction> parse(List<String> lines)
			throws Exception;

}
