# open up a terminal and execute a script in it:
osascript <<END
    tell application "Terminal"
        do script "sm --start REG_WIZARD -f"
    end tell
END

