# Requirements

QAbot verifies Github issues and pull requests according to
[Zerocracy QA rules](https://www.zerocracy.com/policy.html#42)
and submits the verdict in tickets comments.

Verdict comments should be understandable by 0crat bot.

QAbot can be triggered by Github webhook on ticket messages.
If message is a request for review from 0crat bot, then
QAbot takes this ticket for processing.

QAbot has an admin panel, admin may disable verdict auto-reporting
to approve it before submitting. Verdict for ticket is printed
on admin page and admin may click "approve" to send it to ticket.
