package utils;

import com.microsoft.playwright.*;

public class Driver {

    private static final ThreadLocal<Playwright> playwrightPool = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserPool = new ThreadLocal<>();
    private static final ThreadLocal<Page> pagePool = new ThreadLocal<>();

    private Driver() {}

    public static Page getPage() {
        if (pagePool.get() == null) {
            synchronized (Driver.class) {
                String browser = ConfigurationReader.getProperty("browser");
                if (System.getProperty("browser") != null) {
                    System.out.println("Browser type was changed to: " + System.getProperty("browser"));
                    browser = System.getProperty("browser");
                }

                Playwright playwright = Playwright.create();
                playwrightPool.set(playwright);

                BrowserType.LaunchOptions headfulOptions = new BrowserType.LaunchOptions().setHeadless(false);
                BrowserType.LaunchOptions headlessOptions = new BrowserType.LaunchOptions().setHeadless(true);

                Browser browserInstance;
                switch (browser) {
                    case "chrome":
                    case "chromium":
                        browserInstance = playwright.chromium().launch(headfulOptions);
                        break;
                    case "firefox":
                        browserInstance = playwright.firefox().launch(headfulOptions);
                        break;
                    case "webkit":
                    case "safari":
                        browserInstance = playwright.webkit().launch(headfulOptions);
                        break;
                    case "headless-chrome":
                    case "headless-chromium":
                        browserInstance = playwright.chromium().launch(headlessOptions);
                        break;
                    case "firefox-headless":
                        browserInstance = playwright.firefox().launch(headlessOptions);
                        break;
                    default:
                        throw new RuntimeException("No such browser: " + browser);
                }

                browserPool.set(browserInstance);
                Page page = browserInstance.newPage();
                pagePool.set(page);
            }
        }
        return pagePool.get();
    }

    public static void closeDriver() {
        if (pagePool.get() != null) {
            pagePool.get().close();
            pagePool.remove();
        }
        if (browserPool.get() != null) {
            browserPool.get().close();
            browserPool.remove();
        }
        if (playwrightPool.get() != null) {
            playwrightPool.get().close();
            playwrightPool.remove();
        }
    }
}
