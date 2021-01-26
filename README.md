# The Show Status (Intellij Plugin)

## Background
I work at a e-commerce company as a Software Developer. In the code base of our internal systems, used for process automatisation etc., a lot of status ID references are used. For example:

```php
updateOrderStatusTo("1234", "A");
public function updateOrderStatusTo(string $orderId, string newStatus);
```

What is annoying here is that we do not know what status "A" exactly is. It could mean "Shipped" or "Cancelled". To solve this problem I developed an plugin for the Intellij Editor, which we primarily use for development, that shows us the status description when it is selected.
