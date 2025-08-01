/* Handle API Gateway SSL certificate and provide guidance for developers */
(() => {
    // In a production environment, this would be unnecessary as proper SSL certificates would be used
    // This code is for development purposes only to handle self-signed certificates
    if (window.location.hostname === 'localhost') {
        // Show SSL certificate instructions on page load if not previously shown
        if (!localStorage.getItem('sslWarningShown')) {
            console.info('%c⚠️ SSL Certificate Notice', 'font-size: 16px; font-weight: bold; color: #e67e22;');
            console.info(
                '%cTo use this application with the API Gateway, you need to accept the self-signed SSL certificate:\n' +
                '1. Open a new tab and visit https://localhost:8243/vbank/1.0.0\n' +
                '2. Click "Advanced" and then "Proceed to localhost (unsafe)"\n' +
                '3. Return to this page and refresh',
                'font-size: 14px; color: #333;'
            );
            localStorage.setItem('sslWarningShown', 'true');
        }

        // Check if there was a recent SSL error and show a more visible warning
        if (localStorage.getItem('sslErrorDetected') === 'true') {
            // Display SSL certificate acceptance instructions in the DOM
            window.addEventListener('DOMContentLoaded', function() {
                const sslNotice = document.createElement('div');
                sslNotice.style.cssText = `
                    position: fixed;
                    top: 0;
                    left: 0;
                    right: 0;
                    background-color: #f8d7da;
                    color: #721c24;
                    padding: 15px;
                    text-align: center;
                    z-index: 9999;
                    font-family: 'Segoe UI', sans-serif;
                    border-bottom: 1px solid #f5c6cb;
                    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                `;
                
                sslNotice.innerHTML = `
                    <b>⚠️ SSL Certificate Error Detected</b>
                    <p>You need to accept the self-signed SSL certificate for the API Gateway:</p>
                    <ol style="text-align: left; display: inline-block;">
                        <li>Open <a href="https://localhost:8243/vbank/1.0.0" target="_blank">https://localhost:8243/vbank/1.0.0</a> in a new tab</li>
                        <li>Click "Advanced" and then "Proceed to localhost (unsafe)"</li>
                        <li>Return to this page and <a href="javascript:window.location.reload()">refresh</a></li>
                    </ol>
                    <button onclick="this.parentNode.style.display='none'; localStorage.removeItem('sslErrorDetected');" 
                            style="background: #721c24; color: white; border: none; padding: 5px 15px; border-radius: 4px; cursor: pointer;">
                        Dismiss
                    </button>
                `;
                
                document.body.appendChild(sslNotice);
            });
        }
    }
})();
