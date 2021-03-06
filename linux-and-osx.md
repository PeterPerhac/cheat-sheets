# Linux / Unix / OS X

## Terminal

Ctrl+r to search command history
Ctrl+l to get a screenful of blank lines and the command prompt at the top of the screen. (equivalent of `clear` command)
Ctrl+u to erase current command line
Ctrl+s/Ctrl+q to pause and resume a process writing to the console
Ctrl+c to signal **SIGINT** (interrupt) to a process. This is intended for orderly shutdown.
Ctrl+d to signal end of file to a process reading from terminal.
Ctrl+z is used to suspend a process by sending it the signal SIGSTOP, which is like a sleep signal, that can be undone and the process can be resumed again.
    if you accidentally Ctrl+z your application into oblivion, just bring it back to life with the 'fg' command

Ctrl+c tells the terminal to send a **SIGINT** to the current foreground process, which by default translates into terminating the application.
Ctrl+d tells the terminal that it should register an **EOF** on standard input. The **EOF** signal indicates the end of standard input. That is, there will be no more text for the application processing it. Usually that application will, on receiving **EOF**, finish execution and return control to the shell.


## Signals


 Signal     Value     Action   Comment
 -------------------------------------------------------------------------
 SIGHUP        1       Term    Hangup detected on controlling terminal or death of controlling process
 SIGINT        2       Term    Interrupt from keyboard
 SIGQUIT       3       Core    Quit from keyboard
 SIGILL        4       Core    Illegal Instruction
 SIGABRT       6       Core    Abort signal from abort(3)
 SIGFPE        8       Core    Floating point exception
 SIGKILL       9       Term    Kill signal
 SIGSEGV      11       Core    Invalid memory reference
 SIGPIPE      13       Term    Broken pipe: write to pipe with no readers
 SIGALRM      14       Term    Timer signal from alarm(2)
 SIGTERM      15       Term    Termination signal
 SIGUSR1   30,10,16    Term    User-defined signal 1
 SIGUSR2   31,12,17    Term    User-defined signal 2
 SIGCHLD   20,17,18    Ign     Child stopped or terminated
 SIGCONT   19,18,25    Cont    Continue if stopped
 SIGSTOP   17,19,23    Stop    Stop process
 SIGTSTP   18,20,24    Stop    Stop typed at tty
 SIGTTIN   21,21,26    Stop    tty input for background process
 SIGTTOU   22,22,27    Stop    tty output for background process


**SIGINT** is the interrupt signal. The terminal sends it to the foreground process when the user presses ctrl+c. The default behavior is to terminate the process, but it can be caught or ignored. The intention is to provide a mechanism for an orderly, graceful shutdown.

**SIGQUIT** is the dump core signal. The terminal sends it to the foreground process when the user presses ctrl+\. The default behavior is to terminate the process and dump core, but it can be caught or ignored. The intention is to provide a mechanism for the user to abort the process. You can look at **SIGINT** as "user-initiated happy termination" and **SIGQUIT** as "user-initiated unhappy termination."

**SIGTERM** is the termination signal. The default behavior is to terminate the process, but it also can be caught or ignored. The intention is to kill the process, gracefully or not, but to first allow it a chance to cleanup.

**SIGKILL** is the kill signal. The only behavior is to kill the process, immediately. As the process cannot catch the signal, it cannot cleanup, and thus this is a signal of last resort.

**SIGSTOP** is the pause signal. The only behavior is to pause the process; the signal cannot be caught or ignored. The shell uses pausing (and its counterpart, resuming via **SIGCONT**) to implement job control.

**SIGCONT** continue executing if stopped. Useful for resuming stopped jobs. Starting a background job like so:
  ```
  > jekyll serve &
  > jobs
  [1]+  Running                 jekyll serve &
  > fg %1
  <C-z>
  [1]+  Stopped                 jekyll serve
  > kill -CONT %1
  > jobs
  [1]+  Running                 jekyll serve &
  ```


Install `htop` utility as a much better alternative to `top` command:

    brew install htop


# File System Links

_ln_: Creates a Hard Link
The ln (link) utility (without the -s or --symbolic option) creates a hard link to an existing file using the following syntax

    `ln existing-file new-link`

In addition to hard links, Linux supports _symbolic links_, also called soft links or _symlinks_. A hard link is a pointer to a file (the directory entry points to the inode), whereas a symbolic link is an indirect pointer to a file (the directory entry contains the pathname of the pointed-to file -- a pointer to the hard link to the file).

You cannot create a hard link to a directory, but you can create a symbolic link to a directory.

advantage of a symbolic link is that it can point to a _nonexistent_ file. This ability is useful if you need a link to a file that is periodically removed and re-created. A hard link keeps pointing to a "removed" file, which the link keeps alive even after a new file is created.

`ln --symbolic` (or `ln -s`) creates a symbolic link.

in theory there is a tac command that is doing the same as cat but in reverse order... not on a mac, though


# setting up SSH tunneling

```bash
ssh -fNL localhost:8080:localhost:8080 10.255.18.192
```
-f will send it to background and -N will tell it that no command needs to be executed


# Ubuntu specific

To configure some specific parameters of a connected HID device:
    `xinput list`
		to show all input devices
		(note the device ID for below commands)
	`xinput list-props <id>`
		to see all properties of given input device
		(good idea to grep for one that we're interested in)
		e.g.
			`xinput list-props 15 | grep -i finger`
		(note the prop id in brackets)
	`xinput set-prop <deviceID> <propertyID> <newValue>`
		e.g.
			`xinput set-prop 15 297 30 40 100`

