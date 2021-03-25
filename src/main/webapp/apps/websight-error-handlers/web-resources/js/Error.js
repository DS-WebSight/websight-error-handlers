import React from 'react';
import Button from '@atlaskit/button';
import EmptyState from '@atlaskit/empty-state';
import styled from 'styled-components';

const primaryAction = (
    <Button
        appearance='primary'
        onClick={() => window.location = '/'}
    >
        Go Home
    </Button>
);

const ContentContainer = styled.div`
    padding-top: 180px;
    min-height: 476px;
`;

export default class Error extends React.Component {
    render() {
        return (
            <ContentContainer>
                <EmptyState
                    header={this.props.header}
                    description={this.props.description}
                    imageUrl={'/apps/websight-error-handlers/web-resources/images/error-image.png'}
                    primaryAction={primaryAction}
                />
            </ContentContainer>
        );
    }
}