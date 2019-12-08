/*
 * MIT License
 *
 * Copyright (c) 2019 g4s8
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights * to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package wtf.g4s8.qabot.checks;

import java.io.IOException;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 * QA review atomic check for ticket.
 *
 * @since 1.0
 * @todo #1:30min Implement all checks from Zerocracy policy
 *  QA rules section 42. Some checks are related to PRs only
 *  and some to issues, some may have bad quality, some acceptable.
 */
public interface RvCheck {

    /**
     * Run check for ticket.
     * @param ticket Ticket to check
     * @param report QA report
     * @throws IOException On failure
     */
    void run(Ticket ticket, Report report) throws IOException;
}
