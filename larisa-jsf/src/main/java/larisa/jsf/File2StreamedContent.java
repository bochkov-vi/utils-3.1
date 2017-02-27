package larisa.jsf;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import larisa.entity.File;
import larisa.entity.IGetFiles;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;

import java.util.List;

/**
 * Created by home on 27.02.17.
 */
public class File2StreamedContent implements Function<File, StreamedContent> {
    public static final File2StreamedContent instance = new File2StreamedContent();

    public static File2StreamedContent get() {
        return instance;
    }

    @Override
    public StreamedContent apply(File f) {
        if (f != null) {
            return new ByteArrayContent(f.getData(), f.getType(), f.getName(), f.getEncoding());
        }
        return null;
    }

    public List<StreamedContent> transform(List<File> fileList) {
        return Lists.transform(fileList != null ? fileList : ImmutableList.of(), File2StreamedContent.get());
    }

    public List<StreamedContent> getImages(IGetFiles f) {
        if (f != null) {
            return transform(f.getFiles());
        }
        return ImmutableList.of();
    }

    public StreamedContent empty(){
        return new ByteArrayContent(null);
    }
}
