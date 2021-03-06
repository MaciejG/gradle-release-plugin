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

import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.wc.ISVNStatusHandler
import org.tmatesoft.svn.core.wc.SVNStatus
import org.tmatesoft.svn.core.wc.SVNStatusType

class UpToDateChecker implements ISVNStatusHandler {
    boolean hasModifications = false;
    SVNStatusType statusType = null;

    UpToDateChecker() {
    }

    public boolean hasModifications() {
        return hasModifications
    }

    SVNStatusType getStatusType(){
        return statusType
    }
    
    public void handleStatus(SVNStatus status) throws SVNException {
        SVNStatusType statusType = status.getContentsStatus()
        this.statusType = statusType
        if (statusType != SVNStatusType.STATUS_NONE &&
                statusType != SVNStatusType.STATUS_NORMAL &&
                statusType != SVNStatusType.STATUS_IGNORED &&
                statusType != SVNStatusType.STATUS_EXTERNAL &&
                statusType != SVNStatusType.STATUS_UNVERSIONED ) {

            hasModifications = true
        }
    }
}
