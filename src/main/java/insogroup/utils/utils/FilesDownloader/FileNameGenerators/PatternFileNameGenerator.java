package insogroup.utils.utils.FilesDownloader.FileNameGenerators;

import insogroup.utils.utils.FilesDownloader.exceptions.GenerateFileNameException;
import insogroup.utils.utils.FilesDownloader.interfaces.FileNameGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aleksey on 27.05.16.
 */
public class PatternFileNameGenerator implements FileNameGenerator {

    protected Pattern pattern;

    public PatternFileNameGenerator(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String generateName(String url) throws GenerateFileNameException {
        Matcher m = pattern.matcher(url);
        m.find();
        try {
            return m.group();
        } catch (IllegalStateException e) {
            throw new GenerateFileNameException(
                    "Can't generate filename for: '" + url + "' with regexp '" + pattern.toString() + "'");
        }
    }
}
