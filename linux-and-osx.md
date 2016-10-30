# Linux / Unix / OS X

## Terminal

Ctrl+r to search command history
Ctrl+l to scroll down so that command line is the top line (similar effect to `clear` but keeps previous output, it's just out of view)
Ctrl+u to erase current command line
Ctrl+s/Ctrl+q to pause and resume a process writing to the console
Ctrl+c to signal **SIGINT** (interrupt) to a process. This is intended for orderly shutdown. 
Ctrl+d to signal end of file to a process reading from terminal.

Ctrl+c tells the terminal to send a **SIGINT** to the current foreground process, which by default translates into terminating the application.
Ctrl+d tells the terminal that it should register an **EOF** on standard input. The **EOF** signal indicates the end of standard input. That is, there will be no more text for the application processing it. Usually that application will, on receiving **EOF**, finish execution and return control to the shell.


## Signals

**SIGINT** is the interrupt signal. The terminal sends it to the foreground process when the user presses ctrl-c. The default behavior is to terminate the process, but it can be caught or ignored. The intention is to provide a mechanism for an orderly, graceful shutdown.

**SIGQUIT** is the dump core signal. The terminal sends it to the foreground process when the user presses ctrl-\. The default behavior is to terminate the process and dump core, but it can be caught or ignored. The intention is to provide a mechanism for the user to abort the process. You can look at **SIGINT** as "user-initiated happy termination" and **SIGQUIT** as "user-initiated unhappy termination."

**SIGTERM** is the termination signal. The default behavior is to terminate the process, but it also can be caught or ignored. The intention is to kill the process, gracefully or not, but to first allow it a chance to cleanup.

**SIGKILL** is the kill signal. The only behavior is to kill the process, immediately. As the process cannot catch the signal, it cannot cleanup, and thus this is a signal of last resort.

**SIGSTOP** is the pause signal. The only behavior is to pause the process; the signal cannot be caught or ignored. The shell uses pausing (and its counterpart, resuming via **SIGCONT**) to implement job control.

# Other stuff comes here
