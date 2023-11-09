from locust import HttpUser, task, between

class ApiGatewayDemo(HttpUser):
    wait_time = between(2,4)

    @task
    def community_page(self):

        headers = {
        }

        self.client.get('/api/v1/products', headers=headers)