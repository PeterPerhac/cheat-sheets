findInPoms() {
    find $WORKSPACE -name target -prune -o -name "pom.xml" -print | xargs -I{} grep -l $1 {}
}
