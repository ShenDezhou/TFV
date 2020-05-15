import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.nio.file.Paths;

/**
 * 利用lucene获取tf-idf
 *
 * @author yong.chen
 *
 */
public class TfidfView {
    public static final String INDEX_PATH = "F:\\elasticsearch-6.5.4\\data\\nodes\\0\\indices\\B36DGh9MSaih8fTJq8k2PQ\\0\\index";

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
            for (int i = 0; i < reader.numDocs(); i++) {
                int docId = i;
                System.out.println("第" + (i + 1) + "篇文档：");
                Terms terms = reader.getTermVector(docId, "Title");
                if (terms == null)
                    continue;
                TermsEnum termsEnum = terms.iterator();
                BytesRef thisTerm = null;
                while ((thisTerm = termsEnum.next()) != null) {
                    String termText = thisTerm.utf8ToString();
                    System.out.println(termText);
//                    DocsEnum docsEnum = termsEnum.docs(null, null);
//                    while ((docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
//                        System.out.println("termText:" + termText + " TF:  "
//                                + 1.0 * docsEnum.freq() / terms.size());
//                    }
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
//    public void getIDF() {
//        try {
//            Directory directroy = FSDirectory.open(new File(INDEX_PATH));
//            IndexReader reader = DirectoryReader.open(directroy);
//            List<AtomicReaderContext> list = reader.leaves();
//            System.out.println("文档总数 : " + reader.maxDoc());
//            for (AtomicReaderContext ar : list) {
//                String field = "text";
//                AtomicReader areader = ar.reader();
//                Terms terms = areader.terms(field);
//                TermsEnum tn = terms.iterator(null);
//                BytesRef text;
//                while ((text = tn.next()) != null) {
//                    System.out.println("field=" + field + "; text="
//                                    + text.utf8ToString() + "   IDF : "
//                                    + Math.log10(reader.maxDoc() * 1.0 / tn.docFreq())
//                            // + " 全局词频 :  " + tn.totalTermFreq()
//                    );
//                }
//            }
//            reader.close();
//            directroy.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        TfidfView luceneTfIdfUtil = new TfidfView();
        // luceneTfIdfUtil.index();
        luceneTfIdfUtil.getTF();
//        luceneTfIdfUtil.getIDF();
    }
}
