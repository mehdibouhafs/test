package batch;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

/**
 * Created by MBS on 30/06/2016.
 */
public class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(String line) {
        if (line.trim().length() ==0) {
            return false;
        }
        return super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(String record) {
        if (record==null || record.trim().length()==0 ) {
            return null;
        }
        return super.postProcess(record);
    }

}
