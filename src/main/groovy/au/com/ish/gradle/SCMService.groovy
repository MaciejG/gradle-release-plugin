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

abstract public class SCMService {
	protected final def releaseTagPattern = ~/^(\S+)-RELEASE-(\d+)/

	/*
		Are we on a tag or a normal branch
	*/
	def abstract boolean onTag()

	def abstract boolean onGenerateNewTag()

	def abstract String getBaseVersion()

	/*
		The name of the current branch we are on. For svn, this is just the last part of the path to the repo
	*/
	def abstract String getBranchName()

	/*
		A string which represents the SCM commit we are currently on
	*/
	def abstract String getSCMVersion()

	/*
		Returns message that was submitted during this revision commit
	 */
	def abstract String getSCMMessage()

	/*
		The highest release tag in the repository for this branch. For example if the current branch is "master"
		and we have tags called 'master-RELEASE-1' and 'master-RELEASE-2' then this will return 'master-RELEASE-2'
	*/
	def abstract String getLatestReleaseTag(String currentBranch)

    def abstract String getLatestReleaseTagRevision(String currentBranch);

	/*
		Return true if the local checkout has changes which aren't in the remote repository
	*/
	def abstract boolean localIsAheadOfRemote()

	/*
		Return true if there are local changes not committed
	*/
	def abstract boolean hasLocalModifications()

	/*
		Return true if we don't have all the changes from the remote repository
	*/
	def abstract boolean remoteIsAheadOfLocal()

	/*
		Create the tag
	*/
	def abstract performTagging(String tag, String message)

	/*
		Increment the last tag in the repository in order to get the next version to create if the user hasn't supplied one
	*/
	def getNextVersion() {
        def currentBranch = getBranchName()

        def latestReleaseTag = getLatestReleaseTag(currentBranch)

        if (latestReleaseTag) {
	        def tagNameParts = latestReleaseTag.split('-').toList()
	        def latestReleaseTagVersion = tagNameParts[-1]
			def computedNewProjectVersion = project.release.versionStrategy.call(latestReleaseTagVersion, getBaseVersion())
			project.logger.info("tag for given branch (" + currentBranch + ") exists. computed new project version for branch: ${computedNewProjectVersion}")
			return computedNewProjectVersion
        } else {
			def firstProjectVersion = project.release.startVersion.call(getBaseVersion())
			project.logger.info("tag for given branch (" + currentBranch + ") does not exist. first project version for branch: ${firstProjectVersion}")
			return firstProjectVersion
        }
    }
}