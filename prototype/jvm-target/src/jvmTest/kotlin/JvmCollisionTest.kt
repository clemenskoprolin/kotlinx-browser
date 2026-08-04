import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.w3c.dom.HTMLDivElement

class JvmCollisionTest {
    @Test
    fun directOrgW3cStubCannotBeLoadedFromTheClasspath() {
        assertFailsWith<NoClassDefFoundError> {
            TestDivElement()
        }
    }

    @Test
    fun newOrgW3cClassifierIsHiddenFromClassLookup() {
        assertFailsWith<ClassNotFoundException> {
            Class.forName("org.w3c.dom.HTMLDivElement")
        }
    }
}

private class TestDivElement : HTMLDivElement()
