package com.irbraga.notes.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record NoteDto(@Size(min = 1, max = 50) String title,
                      String body,
                      @Size(min = 7, max = 7) String color) {
    /**
     * Color Hex validation with Regex.
     * => https://mkyong.com/regular-expressions/how-to-validate-hex-color-code-with-regular-expression/
     */
    private static final String HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    private static final Pattern pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);
    
    @AssertTrue(message = "Hex color is not valid.")
    public boolean isColorHexColorValid() {
        if (this.color != null) {
            Matcher matcher = pattern.matcher(this.color);
            return matcher.matches();
        }
        return true;
    }

    /**
     * Checking if one of the two fields were inform.
     * @return
     */
    @AssertFalse(message = "Inform either title or body.")
    public boolean isTitleOrBodyEmpty() {
        return (this.title == null || "".equals(this.title.trim())) && (this.body == null || "".equals(this.body.trim()));
    }
}
