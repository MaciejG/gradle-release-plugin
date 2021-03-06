/*
 * Copyright 2012 ish group pty ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.com.ish.gradle

class ReleasePluginExtension {

    private versionStrategy = { latestReleaseTagVersion, baseVersion ->
        def versioningParts = latestReleaseTagVersion.split('\\.')

        boolean sameBaseVersion = true
        if (baseVersion) {
            def baseVersioningParts = baseVersion.split('\\.')
            for (int i = 0; i < baseVersioningParts.size(); i++) {
                if (baseVersioningParts.getAt(i) != versioningParts.getAt(i)) {
                    sameBaseVersion = false
                    break;
                }
            }
        }

        if (sameBaseVersion) {
            def newVersioningParts = versioningParts.take(versioningParts.length - 1) + (++(versioningParts.last().toInteger()))
            return newVersioningParts.join('.')
        } else {
            return startVersion.call(baseVersion)
        }
    }
    private startVersion = { baseVersion ->
        if (baseVersion) {
            baseVersion + '0'
        } else {
            "1.0.0"
        }
    }

    private final ReleasePlugin plugin

    private boolean failOnSnapshotDependencies = true
    private String scm
    private String username
    private String password
    private boolean releaseDryRun = false
    private boolean allowLocalModifications = false
    private boolean generateNewVersion = false
    private String baseVersion
    private String extraMsg

    public ReleasePluginExtension(ReleasePlugin plugin) {
        this.plugin = plugin
    }

    def getVersionStrategy() {
        return versionStrategy
    }

    def getStartVersion() {
        return startVersion
    }

    /*
      Read only property for getting the version of this project.
      The version is derived from the following rules:
      1. If the releaseVersion property was passed to gradle via the -P command line option, then use that.
      2. If the version control system is currently pointing to a tag, then use a version derived from the name of the tag
      3. Use the name of the branch (or trunk/head) as the version appended with "-SNAPHOT"
    */

    public getProjectVersion() {
        return plugin.projectVersion
    }

    /*
      Read only property for getting the version which the source control system is pointing to.
    */

    public String getScmVersion() {
        return plugin.getSCMVersion()
    }

    public String getScmMessage() {
        return plugin.getSCMMessage()
    }

    public boolean getHasLocalModifications() {
        return plugin.getHasLocalModifications()
    }

    /*
      Get the previously set value for this property
    */

    public boolean getFailOnSnapshotDependencies() {
        return failOnSnapshotDependencies
    }

    /*
      A configurable option which defaults to true. Will fail the release task if any dependency is
      currently pointing to a SNAPSHOT
    */

    public setFailOnSnapshotDependencies(boolean failOnSnapshotDependencies) {
        this.failOnSnapshotDependencies = failOnSnapshotDependencies
    }

    /*
      Define the type of version control system in use for this project. Current valid values are:
      * svn
      * git
    */

    public setScm(String scm) {
        this.scm = scm
    }

    /*
      Get the previously set value for this property
    */

    public getScm() {
        return scm
    }

    /*
      Define the scm username
    */

    public setUsername(String username) {
        this.username = username
    }

    /*
      Get the previously set value for this property
    */

    public getUsername() {
        return username
    }

    /*
      Define the scm password
    */

    public setPassword(String password) {
        this.password = password
    }

    /*
      Get the previously set value for this property
    */

    public getPassword() {
        return password
    }

    /*
      Set simulate variable, releaseDryRun == true means no actual commit to the scm
    */

    public setReleaseDryRun(boolean releaseDryRun) {
        this.releaseDryRun = releaseDryRun
    }

    /*
      Get the previously set value for this property
    */

    public getReleaseDryRun() {
        return releaseDryRun
    }

    /*
      Set allowLocalModifications variable, allowing to skip the working copy for local changes
    */

    public setAllowLocalModifications(boolean allowLocalModifications) {
        this.allowLocalModifications = allowLocalModifications
    }

    /*
      Get the previously set value for this property
    */

    public getAllowLocalModifications() {
        return allowLocalModifications
    }

    public setGenerateNewVersion(boolean generateNewVersion) {
        this.generateNewVersion = generateNewVersion
    }

    public getGenerateNewVersion() {
        return generateNewVersion;
    }

    String getBaseVersion() {
        return baseVersion
    }

    void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion
    }

    String getExtraMsg() {
        return extraMsg
    }

    void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg
    }
}