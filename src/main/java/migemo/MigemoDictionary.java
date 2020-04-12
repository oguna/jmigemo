package migemo;

import java.io.*;
import java.util.*;

public class MigemoDictionary {
    private String[] keys;
    private String[] values;
    private TreeMap<String, String> tempDictionary = new TreeMap<>();
    private MigemoCompactDictionary compactDictionary = null;

    public void load(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            load(fis);
        }
    }

    public void loadDefault() {
        try (InputStream is = MigemoDefaultCompactDictionary.getStream();
             BufferedInputStream bis = new BufferedInputStream(is)) {
            this.compactDictionary = new MigemoCompactDictionary(bis);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void load(InputStream is) throws IOException {
        Objects.requireNonNull(is);
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(";") && !line.isEmpty()) {
                    int semicolonPos = line.indexOf('\t');
                    String key = line.substring(0, semicolonPos);
                    String value = line.substring(semicolonPos + 1, line.length());
                    tempDictionary.put(key, value);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void build() {
        if (keys != null) {
            throw new UnsupportedOperationException("dictionary has already built");
        }
        this.keys = new String[tempDictionary.size()];
        this.values = new String[tempDictionary.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : this.tempDictionary.entrySet()) {
            this.keys[index] = entry.getKey();
            this.values[index] = entry.getValue();
            index++;
        }
        this.tempDictionary.clear();
        this.tempDictionary = null;
    }

    /**
     * 指定したキーで始まる単語のよみを持つ漢字リストのエントリを検索しその結果の漢字リストを返す
     * @param hiragana キー
     * @return 配列で表した検索結果の漢字リスト
     */
    public String[] predictiveSearch(String hiragana) {
        String[] compactDictResults;
        if (compactDictionary != null) {
            compactDictResults = compactDictionary.predictiveSearch(hiragana);
            if (compactDictResults == null) {
                compactDictResults = new String[0];
            }
        } else {
            compactDictResults = new String[0];
        }
        List<String> userDictResults;
        if (keys.length > 0) {
            String stop = hiragana.substring(0, hiragana.length() - 1) + (char) (hiragana.charAt(hiragana.length() - 1) + 1);
            int startPos = Arrays.binarySearch(this.keys, hiragana);
            if (startPos < 0) {
                startPos = -(startPos + 1);
            }
            int endPos = Arrays.binarySearch(this.keys, stop);
            if (endPos < 0) {
                endPos = -(endPos + 1);
            }
            List<String> userDictItems = new ArrayList<>();
            for (int i = startPos; i < endPos; i++) {
                userDictItems.addAll(Arrays.asList(this.values[i].split("\t")));
            }
            userDictResults = userDictItems;
        } else {
            userDictResults = Collections.emptyList();
        }
        String[] results = new String[compactDictResults.length + userDictResults.size()];
        System.arraycopy(compactDictResults, 0, results, 0, compactDictResults.length);
        for (int i = 0; i < userDictResults.size(); i++) {
            results[compactDictResults.length + i] = userDictResults.get(i);
        }
        return results;
    }
}
