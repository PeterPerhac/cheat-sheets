"Shareable" files are those that can be stored on one host and used on others. e.g. files in user home directories are shareable whereas device lock files are not.

"Static" files include binaries, libraries, documentation files and other files that do not change without system administrator intervention. "Variable" files are files that are not static.

Historical UNIX-like filesystem hierarchies contained both static and variable files under both /usr and /etc. In order to realize the advantages mentioned above, the /var hierarchy was created and all _variable files_ were transferred from /usr to _/var_. Consequently /usr can now be mounted read-only (if it is a separate filesystem). Variable files have been transferred from /etc to /var over a longer period as technology has permitted.


        shareable	        unshareable
static  
        /usr	            /etc
        /opt	            /boot
variable
        /var/mail	        /var/run
 	    /var/spool/news	    /var/lock

bin	Essential command binaries
boot	Static files of the boot loader
dev	Device files
etc	Host-specific system configuration
lib	Essential shared libraries and kernel modules
media	Mount point for removeable media
mnt	Mount point for mounting a filesystem temporarily
opt	Add-on application software packages
sbin	Essential system binaries
srv	Data for services provided by this system
tmp	Temporary files
usr	Secondary hierarchy
var	Variable data

