package nl.vintik.example.java.junit4.parametarized;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static nl.vintik.example.java.junit4.parametarized.GuiCookieFilter.GUI_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@RunWith(Enclosed.class)
public class GuiCookieFilterTest {
    @RunWith(MockitoJUnitRunner.class)
    public static class GuiCookieFilterRegularTest {

        private GuiCookieFilter filterUnderTest;

        private MockHttpServletResponse response;
        private MockHttpServletRequest request;

        @Mock
        private FilterChain filterChain;
        @Mock
        private CookieHelper cookieHelper;

        private int randomizerInvocation = 0;
        private BiFunction<Integer, String, String> randomizer = (a, b) -> String.format("%d-%s-%d", a, b, randomizerInvocation++);

        @Before
        public void setup() {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();

            randomizerInvocation = 0;
            request.setRequestURI("/nl/");

            filterUnderTest = new GuiCookieFilter();
            filterUnderTest.init(randomizer, cookieHelper);
        }

        @Test
        public void invalidCookieAlsoSetsRequestAttribute() throws IOException, ServletException {
            filterUnderTest.doFilter(request, response, filterChain);

            assertNotNull(request.getAttribute(GUI_NAME));
        }

        @Test
        public void doTestRandomizer() {
            assertEquals(this.randomizer.apply(1, "b"), "1-b-0");
            assertEquals(this.randomizer.apply(1, "b"), "1-b-1");
        }

        @Test
        public void doFilterEmpty() throws IOException, ServletException {
            filterUnderTest.doFilter(request, response, filterChain);

            assertEquals("32-0123456789abcdefghijklmnopqrstuvwxyz-0", response.getCookie(GUI_NAME).getValue());
            verify(filterChain, times(1)).doFilter(request, response); // show must go on
        }

        @Test
        public void doFilterWhenSetDontUpdate() throws IOException, ServletException {
            filterUnderTest.doFilter(request, response, filterChain);

            String cookieValue = response.getCookie(GUI_NAME).getValue();
            Cookie[] cookie = new Cookie[]{new Cookie(GUI_NAME, cookieValue)};

            when(cookieHelper.updateCookie(cookieValue)).thenReturn(cookieValue);

            MockHttpServletRequest secondRequest = new MockHttpServletRequest();
            MockHttpServletResponse secondResponse = new MockHttpServletResponse();
            secondRequest.setCookies(cookie);

            filterUnderTest.doFilter(secondRequest, secondResponse, filterChain);

            assertEquals("32-0123456789abcdefghijklmnopqrstuvwxyz-0", response.getCookie(GUI_NAME).getValue());
            assertEquals("32-0123456789abcdefghijklmnopqrstuvwxyz-0", secondResponse.getCookie(GUI_NAME).getValue());

            verify(filterChain, times(1)).doFilter(request, response);
            verify(filterChain, times(1)).doFilter(secondRequest, secondResponse);
            verify(cookieHelper, times(1)).updateCookie(cookieValue);
        }
    }

    @RunWith(Parameterized.class)
    public static class GuiCookieFilterParameterizedTest {

        private GuiCookieFilter filterUnderTest;

        private MockHttpServletResponse response;
        private MockHttpServletRequest request;

        private FilterChain filterChain = mock(FilterChain.class);
        private CookieHelper removeIPAddressFromBUI = new CookieHelper();

        private int randomizerInvocation = 0;
        private BiFunction<Integer, String, String> randomizer = (a, b) -> String.format("%d-%s-%d", a, b, randomizerInvocation++);

        @Before
        public void setup() {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();

            randomizerInvocation = 0;

            request.setRequestURI("/nl/");

            filterUnderTest = new GuiCookieFilter();
            filterUnderTest.init(randomizer, removeIPAddressFromBUI);
        }

        @Parameterized.Parameters(name = "Test {index} -> From \"{0}\" to \"{1}\"")
        public static Iterable<String[]> data() {
            List<String[]> testCases = new ArrayList<>();

            testCases.add(new String[]{"131.211.85.1.74893927343", "131.211.85.1.74893927343"});
            testCases.add(new String[]{"vynxua247pfc69r4jlk04lq13ycfvw52", "vynxua247pfc69r4jlk04lq13ycfvw52"});
            testCases.add(new String[]{"1234567780kjhkjhadsfkadsf", "1234567780kjhkjhadsfkadsf"});
            return testCases;
        }

        @Parameterized.Parameter(0)
        public String inputGUI;

        @Parameterized.Parameter(1)
        public String expectedOutputGUI;

        @Test
        public void doFilterWhenSetDontUpdate() throws IOException, ServletException {
            Cookie[] cookie = new Cookie[]{new Cookie(GUI_NAME, inputGUI)};
            request.setCookies(cookie);

            filterUnderTest.doFilter(request, response, filterChain);

            assertEquals(expectedOutputGUI, response.getCookie(GUI_NAME).getValue());
            verify(filterChain, times(1)).doFilter(request, response);
        }
    }

}
