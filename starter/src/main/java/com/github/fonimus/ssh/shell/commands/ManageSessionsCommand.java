/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fonimus.ssh.shell.commands;

import com.github.fonimus.ssh.shell.SimpleTable;
import com.github.fonimus.ssh.shell.SshShellHelper;
import com.github.fonimus.ssh.shell.manage.SshShellSessionManager;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Arrays;
import java.util.Map;

import static com.github.fonimus.ssh.shell.SshShellProperties.SSH_SHELL_PREFIX;
import static com.github.fonimus.ssh.shell.manage.SshShellSessionManager.sessionUserName;

/**
 * Command to manage ssh sessions, not available by default
 */
@SshShellComponent
@ShellCommandGroup("Manage Sessions Commands")
@ConditionalOnProperty(
        value = SSH_SHELL_PREFIX + ".default-commands.manage-sessions", havingValue = "true"
)
public class ManageSessionsCommand {

    private final SshShellHelper helper;

    private final SshShellSessionManager sessionManager;

    public ManageSessionsCommand(SshShellHelper helper, @Lazy SshShellSessionManager sessionManager) {
        this.helper = helper;
        this.sessionManager = sessionManager;
    }

    @ShellMethod("Displays active sessions")
    public String manageSessionsList() {
        Map<Long, ChannelSession> sessions = sessionManager.listSessions();

        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("Session Id").column("Local address").column("Remote address").column("Authenticated User");

        for (ChannelSession value : sessions.values()) {
            builder.line(Arrays.asList(
                    value.getServerSession().getIoSession().getId(),
                    value.getServerSession().getIoSession().getLocalAddress(),
                    value.getServerSession().getIoSession().getRemoteAddress(),
                    sessionUserName(value)
            ));
        }
        return helper.renderTable(builder.build());
    }

    @ShellMethod("Displays session")
    public String manageSessionsInfo(@ShellOption(value = {"-i", "--session-id"}) long sessionId) {
        ChannelSession session = sessionManager.getSession(sessionId);
        if (session == null) {
            return helper.getError("Session [" + sessionId + "] not found");
        }
        return helper.getSuccess(sessionTable(session.getServerSession()));
    }

    @ShellMethod("Stop session")
    public String manageSessionsStop(@ShellOption(value = {"-i", "--session-id"}) long sessionId) {
        return sessionManager.stopSession(sessionId) ?
                helper.getSuccess("Session [" + sessionId + "] stopped") :
                helper.getWarning("Unable to stop session [" + sessionId + "], maybe it does not exist");
    }

    private String sessionTable(ServerSession session) {
        return helper.renderTable(SimpleTable.builder()
                .column("Property").column("Value")
                .line(Arrays.asList("Session id", session.getIoSession().getId()))
                .line(Arrays.asList("Local address", session.getIoSession().getLocalAddress()))
                .line(Arrays.asList("Remote address", session.getIoSession().getRemoteAddress()))
                .line(Arrays.asList("Server version", session.getServerVersion()))
                .line(Arrays.asList("Client version", session.getClientVersion()))
                .build());
    }
}
