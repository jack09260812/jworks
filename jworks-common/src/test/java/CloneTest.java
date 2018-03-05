import com.github.utils.clone.CloneUtil;
import com.github.utils.clone.Cloneable;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by jinwei.li on 2017/8/28 0028.
 */
public class CloneTest {


    @Test(timeout = 100)
    public void testCloneable() throws IOException {
        Toy t1 = new Toy();
        Toy t2 = CloneUtil.simpleClone(t1);
        System.out.println(t2.name);
        System.out.println(t1.properties==t2.properties);
    }

    static class Toy implements Cloneable<Toy>,Serializable {
        private String name = "toy";
        private Map<String, String> properties = ImmutableMap.of("color", "red", "size", "small");

        @Override
        public Toy clone() {
            try {
                return (Toy) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
