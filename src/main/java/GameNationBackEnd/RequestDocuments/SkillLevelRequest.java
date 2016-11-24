package GameNationBackEnd.RequestDocuments;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by lucas on 24/11/2016.
 */
public class SkillLevelRequest {
    @JsonInclude
    public int level;
}
