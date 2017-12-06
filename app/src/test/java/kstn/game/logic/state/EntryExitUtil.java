package kstn.game.logic.state;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EntryExitUtil {
    public static void assertSetUpEntryExit(
            IEntryExit system, IEntryExit mockedDepend) {
        verify(mockedDepend, never()).entry();
        system.entry();
        verify(mockedDepend).entry();
        verify(mockedDepend, never()).exit();
        system.exit();
        verify(mockedDepend).exit();
    }
}
