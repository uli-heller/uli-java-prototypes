package org.uli.gcb;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import java.util.concurrent.TimeUnit;

public class GuavaCache {
    private CacheLoader<Pair, String>  myCacheLoader = new CacheLoader<Pair, String>() {
	public String load(Pair pair) throws Exception {
	    return GuavaCache.this.loadPair(pair.a, pair.b);
        }
    };
    private LoadingCache<Pair, String> myCache = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
	.build(myCacheLoader);

    @Data
    private static class Pair {
	int a;
	int b;
	public Pair(int a, int b) {
	    this.a = a;
	    this.b = b;
	}
    }

    static int cnt = 0;

    private String loadPair(int a, int b) {
	++cnt;
	return "this pair is ("+a+","+b+") : "+cnt;
    }

    public String getPair(int a, int b) throws Exception {
	return this.myCache.get(new Pair(a, b));
    }
}
