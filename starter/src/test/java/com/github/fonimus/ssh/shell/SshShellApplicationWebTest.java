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

package com.github.fonimus.ssh.shell;

import com.github.fonimus.ssh.shell.conf.SshShellSessionConfigurationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SshShellApplicationWebTest.class, SshShellSessionConfigurationTest.class},
        properties = {
                "ssh.shell.port=2346",
                "ssh.shell.password=pass",
                "management.endpoints.web.exposure.include=*"
        }
)
@ExtendWith(SpringExtension.class)
@SpringBootApplication
@DirtiesContext
public class SshShellApplicationWebTest
        extends AbstractCommandTest {

    @Test
    void testCommandAvailability() {
        setActuatorRole();

        super.commonCommandAvailability();

        assertTrue(cmd.sessionsAvailability().isAvailable());
    }

    @Test
    void testCommandAvailabilityWithoutRole() {
        setRole("USER");

        assertAll(
                () -> assertFalse(cmd.auditAvailability().isAvailable()),
                () -> assertFalse(cmd.beansAvailability().isAvailable()),
                () -> assertFalse(cmd.conditionsAvailability().isAvailable()),
                () -> assertFalse(cmd.configpropsAvailability().isAvailable()),
                () -> assertFalse(cmd.envAvailability().isAvailable()),
                () -> assertFalse(cmd.healthAvailability().isAvailable()),
                () -> assertTrue(cmd.infoAvailability().isAvailable()),
                () -> assertFalse(cmd.loggersAvailability().isAvailable()),
                () -> assertFalse(cmd.metricsAvailability().isAvailable()),
                () -> assertFalse(cmd.mappingsAvailability().isAvailable()),
                () -> assertFalse(cmd.scheduledtasksAvailability().isAvailable()),
                () -> assertFalse(cmd.shutdownAvailability().isAvailable()),
                () -> assertFalse(cmd.threaddumpAvailability().isAvailable())
        );
    }
}
