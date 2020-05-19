import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 利用lucene获取tf-idf
 *
 * @author pkulaw
 *
 */
public class TfidfView {
    public static final String INDEX_PATH = "F:\\elasticsearch-6.5.4\\data\\nodes\\0\\indices\\B36DGh9MSaih8fTJq8k2PQ\\0\\index";
    public String[] fields = {"Title"};
//    public void index() {
//        try {
//            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
//            Directory directory = FSDirectory.open(new File(INDEX_PATH));
//            IndexWriterConfig config = new IndexWriterConfig(
//                    Version.LUCENE_CURRENT, analyzer);
//            IndexWriter iwriter = new IndexWriter(directory, config);
//            FieldType ft = new FieldType();
//            ft.setIndexed(true);// 存储
//            ft.setStored(true);// 索引
//            ft.setStoreTermVectors(true);
//            ft.setTokenized(true);
//            ft.setStoreTermVectorPositions(true);// 存储位置
//            ft.setStoreTermVectorOffsets(true);// 存储偏移量
//            Document doc = new Document();
//            String text = "This is the text to be indexed.";
//            doc.add(new Field("text", text, ft));
//            iwriter.addDocument(doc);
//            doc = new Document();
//            text = "I am the text to be stored.";
//            doc.add(new Field("text", text, ft));
//            iwriter.addDocument(doc);
//            iwriter.forceMerge(1);// 最后一定要合并为一个segment，不然无法计算idf
//            iwriter.close();
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 读取索引，显示词频
     *
     * **/
    public void getTF() {
        try {
            Directory directroy = FSDirectory.open(Paths.get(INDEX_PATH));
            IndexReader reader = DirectoryReader.open(directroy);
            System.out.println("文档总数 : " + reader.maxDoc());
            System.out.println("字段 : " + Arrays.toString(fields));
            for (int i = 0; i < reader.numDocs(); i++) {
                int docId = i;
                System.out.println("第" + (i + 1) + "篇文档：");
                for(String field: fields) {
//                    System.out.println("----"+field+"----");
                    Terms terms = reader.getTermVector(docId, field);
                    if (terms == null)
                        continue;
                    System.out.println("文档含词数："+terms.getSumDocFreq());

                    TermsEnum termsEnum = terms.iterator();
                    BytesRef thisTerm = null;
                    while ((thisTerm = termsEnum.next()) != null) {
                        String termText = thisTerm.utf8ToString();
                        System.out.println(termText+", 词频:"+ termsEnum.docFreq());
                    }
                }
            }
            reader.close();
            directroy.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 计算IDF
     *
     * *
     */
    public void getIDF() {
        try {
            Directory directroy = FSDirectory.open(Paths.get(INDEX_PATH));
            IndexReader reader = DirectoryReader.open(directroy);
            List<LeafReaderContext> list = reader.leaves();
            System.out.println("文档总数 : " + reader.maxDoc());
            System.out.println("field=" + Arrays.toString(fields));
            for (LeafReaderContext ar : list) {
                LeafReader areader = ar.reader();
                Terms terms = areader.terms(fields[0]);
                TermsEnum tn = terms.iterator();
                BytesRef text=null;
                while ((text = tn.next()) != null) {
                    System.out.println("词=" + text.utf8ToString() +", DF(包含该词的文档数):"+tn.docFreq()+ ", IDF : "+ Math.log10(reader.maxDoc() * 1.0 / tn.docFreq())+ ", 全局词频:  " + tn.totalTermFreq());
                }
            }
            reader.close();
            directroy.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TfidfView luceneTfIdfUtil = new TfidfView();
        // luceneTfIdfUtil.index();
        luceneTfIdfUtil.getTF();
        luceneTfIdfUtil.getIDF();
    }
}
