# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Set this with the path to your assignments repo. Use https protocol and a public
# repo, or see assignment instructions for use with ssh keys
SRC_URI = "git://github.com/cu-ecen-aeld/assignment-3-rumo3998.git;protocol=https;branch=main" 

PV = "1.0+git${SRCPV}"
# set to reference a specific commit hash in your assignment repo
SRCREV = "4a486df22941d611b5a5c5e05b01ebdc9e7c813e"

# Inherit update-rc.d class to automate the registration of system boot initscripts
inherit update-rc.d

# Configure runtime initscript variables for update-rc.d
INITSCRIPT_NAME = "aesdsocket-start-stop"
INITSCRIPT_PARAMS = "defaults 99"

# Add the aesdsocket application and any other files you will install in do_install below
# See https://github.com/openembedded/openembedded-core/blob/wrynose/meta/conf/bitbake.conf for path prefixes like ${bindir}
# or ${sysconfdir}
FILES:${PN} += "${bindir}/aesdsocket"
FILES:${PN} += "${sysconfdir}/init.d/aesdsocket-start-stop"

# customize these as necessary for any libraries you need for your application
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
        :
}

do_compile () {
        # Switch to the server directory where your source code to be built is located
        oe_runmake -C ${S}/server all
}

do_install () {
        # Install your binaries/scripts here.
        # Be sure to install the target directory with install -d first
        # Yocto variables ${D} and ${S} are useful here
        
        # 1. Staging and installing the daemon binary into /usr/bin/
        install -d ${D}${bindir}
        install -m 0755 ${S}/server/aesdsocket ${D}${bindir}/

        # 2. Staging and installing the SysVinit script into /etc/init.d/
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${S}/server/aesdsocket-start-stop ${D}${sysconfdir}/init.d/aesdsocket-start-stop
}
